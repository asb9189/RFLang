package evaluator

import nodes.Program
import nodes.interfaces.Expression
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.statements.VarDec

class Evaluator (program: Program) {

    private val program: Program
    private var variableCount: Int
    private var varToInt: HashMap<String, Int>
    private var symbolTable: HashMap<Int, Value>

    init {
        this.program = program
        this.variableCount = 0
        this.varToInt = HashMap()
        this.symbolTable = HashMap()
    }

    fun run() {
        for (stmt in program.getStatements()) {
            executeStatement(stmt)
        }
    }

    private fun executeStatement(statement: Statement) {
        when (statement.getType()) {
            StatementType.VAR_DEC_STMT -> executeVarDecStmt(statement as VarDec)
            StatementType.VAR_ASSIGN_STMT -> {}
            StatementType.WHILE_STMT -> {}
            StatementType.REPEAT_STMT -> {}
            StatementType.RETURN_STMT -> {}
            StatementType.FUNC_CALL_STMT -> {}
            StatementType.FUNC_DEF_STMT -> {}
        }
    }

    private fun executeVarDecStmt(varDec: VarDec) {
        val varName: String = varDec.getVarName()
        val value: Pair<Any, ValueType> = varDec.getValue().eval()

        println("$varName = ${value.first}")
    }
}