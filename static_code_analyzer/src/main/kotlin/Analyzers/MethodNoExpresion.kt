package Analyzers

import Analyzer
import InvalidInputException
import ast.node.ASTNode
import ast.node.MethodCall
import ast.node.Value
import token.Location

class MethodNoExpresion: Analyzer {
    override fun analyze(astNode: ASTNode) {
        when{
            astNode is MethodCall -> analyze(astNode.arguments, astNode.identifier.location)
        }
    }

    private fun analyze(value: Value, location: Location){
        if (value.tree.right == null && value.tree.left == null){
            return
        }else{
            throw InvalidInputException("Invalid expression in row ${location.row} ${location.column}")
        }
    }

}