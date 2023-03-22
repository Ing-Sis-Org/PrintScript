import utilities.*

class StatementParserProvider {


    private val initializationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isInitialization,
        this::createInitializationNode
    )
    private val declarationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isDeclaration,
        this::createDeclarationNode
    )
    private val assignationParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isAssignation,
        this::createAssignationNode
    )
    private val methodCallParser: Pair<SyntaxVerifier, ASTTreeConstructor> = Pair(
        this::isMethodCall,
        this::createMethodNode
    )

    private val printScriptParser = listOf(
        initializationParser, assignationParser, methodCallParser)

    private val parserMap = mapOf(
        Pair("printScript", printScriptParser)
    )

    fun getParserList(name: String): List<Pair<SyntaxVerifier, ASTTreeConstructor>>? {
        return parserMap[name]
    }

    private fun createMethodNode(statement: List<Token>): MethodCall {
        checkMethodCall(statement)
        val parameterValue = createValueNode(statement.subList(2, statement.size))
        return MethodCall(statement[0], parameterValue)
    }

    private fun checkMethodCall(statement: List<Token>) {
        checkMinLength(statement)
        checkIdentifier(statement[0])
        checkLeftParenthesis(statement[1])
        checkRightParenthesis(statement[statement.size-1])
    }

    private fun checkMinLength(statement: List<Token>) {
        if (statement.size < 4) throw MalformedStructureException("Missing arguments at line: ${statement[0].location.row}")
    }

    private fun checkRightParenthesis(token: Token) {
        if (token.type != TokenType.RIGHT_PARENTHESIS)
            throw UnexpectedTokenException("')' expected at: ${token.location.row}, ${token.location.column}")
    }

    private fun checkLeftParenthesis(token: Token) {
        if (token.type != TokenType.LEFT_PARENTHESIS)
            throw UnexpectedTokenException("'(' expected at: ${token.location.row}, ${token.location.column}")
    }

    private fun isMethodCall(statement: List<Token>): Boolean {
        return statement[0].type == TokenType.IDENTIFIER
    }

    private fun createAssignationNode(statement: List<Token>): Assignation {
        checkAssignation(statement)
        val value = createValueNode(statement.subList(2, statement.size))
        return Assignation(statement[0], value)
    }

    private fun checkAssignation(statement: List<Token>) {
        checkIdentifier(statement[0])
        checkValueNode(statement.subList(2, statement.size))
    }

    private fun isAssignation(statement: List<Token>): Boolean {
        if(statement.size < 2) return false
        return statement[1].type == TokenType.ASIGNATION_EQUALS
    }

    private fun createInitializationNode(statement: List<Token>): DeclarationInitalization {
        val declaration = createDeclarationNode(statement.subList(0, 4))
        val value = createValueNode(statement.subList(5, statement.size))
        return DeclarationInitalization(declaration, value)
    }

    private fun checkValueNode(statement: List<Token>) {
        TODO("Not yet implemented")
    }

    private fun createValueNode(statement: List<Token>): Value {
        checkValueNode(statement)
        /*if (statement.size == 1)*/ return Value(BinaryTokenNode(statement[0], null, null))
    }


    private fun isInitialization(statement: List<Token>): Boolean {
        if (statement.size < 5) return false
        return statement[4].type == TokenType.ASIGNATION_EQUALS
    }

    private fun createDeclarationNode(statement: List<Token>): Declaration{
        checkDeclaration(statement)
        return Declaration(statement[1], statement[3])
    }

    private fun checkDeclaration(statement: List<Token>) {
        checkLength(statement, 4, "declaration")
        checkLetKeyword(statement[0])
        checkIdentifier(statement[1])
        checkDoubleDots(statement[2])
        checkType(statement[3])
    }

    private fun checkLetKeyword(token: Token) {
        if (token.type != TokenType.LET_KEYWORD)
            throw UnexpectedTokenException("Let expected at: ${token.location.row}, ${token.location.column}")
    }

    private fun checkLength(statement: List<Token>, correctSize: Int, structureName: String) {
        if (statement.size < correctSize) throw MalformedStructureException("Malformed $structureName at ${statement[0].location.row} ")
        if (statement.size > correctSize) throw UnexpectedTokenException("Unexpected token at ${statement[4].location.row}, ${statement[4].location.column}")
    }

    private fun isDeclaration(statement: List<Token>): Boolean{
        return statement[0].type == TokenType.LET_KEYWORD
    }

    private fun checkIdentifier(token: Token) {
        if (token.type != TokenType.IDENTIFIER)
            throw UnexpectedTokenException("Identifier expected at: ${token.location.row}, ${token.location.column}")
    }
    private fun checkDoubleDots(token: Token) {
        if (token.type != TokenType.DOUBLE_DOTS)
            throw UnexpectedTokenException("Double dots expected at: ${token.location.row}, ${token.location.column}")
    }
    private fun checkType(token: Token) {
        if (token.type != TokenType.NUMBER_LITERAL && token.type != TokenType.STRING_LITERAL)
            throw UnexpectedTokenException("Type expected at: ${token.location.row}, ${token.location.column}")
    }
}


typealias SyntaxVerifier = (statement: List<Token>) -> Boolean
typealias ASTTreeConstructor = (statement: List<Token>) -> ASTNode