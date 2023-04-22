package node

import ast.node.ASTNode

interface ASTNProvider {

    fun readASTNode() : ASTNProviderResponse
}

interface ASTNProviderResponse
data class ASTNProviderResponseSuccess(val astNode: ASTNode) : ASTNProviderResponse
data class ASTNProviderResponseError(val error: String) : ASTNProviderResponse
class ASTNProviderResponseEnd : ASTNProviderResponse