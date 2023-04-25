package parser

import astBuilders.ASTBuilder
import astBuilders.AssignationASTBuilder
import astBuilders.DeclarationASTBuilder
import astBuilders.DeclarationInitializationASTBuilder
import astBuilders.MethodCallASTBuilder
import exceptions.MalformedStructureException
import node.ASTNode
import token.Token
import token.TokenType

class RegularParser(private val astBuilderList: List<ASTBuilder<ASTNode>>) : Parser {

    override fun parse(tokens: List<Token>): ASTNode {
        return createChild(tokens)
    }

    private fun createChild(
        statement: List<Token>
    ): ASTNode {
        for (builder in astBuilderList) {
            if (builder.isApplicable(statement)) {
                return builder.buildAST(statement)
            }
        }
        throw MalformedStructureException("Could not recognize syntax")
    }

    private fun breakIntoStatements(tokens: List<Token>): List<List<Token>> {
        var lastIndex = 0
        val statements: MutableList<List<Token>> = mutableListOf()
        for (i in tokens.indices) {
            if (tokens[i].type == TokenType.SEMICOLON) {
                statements.add(tokens.subList(lastIndex, i))
                lastIndex = i + 1
            }
        }
        return statements
    }

    companion object {
        fun createDefaultParser(): RegularParser {
            return RegularParser(
                listOf(
                    DeclarationInitializationASTBuilder(),
                    MethodCallASTBuilder(),
                    AssignationASTBuilder(),
                    DeclarationASTBuilder()
                )
            )
        }
    }
}
