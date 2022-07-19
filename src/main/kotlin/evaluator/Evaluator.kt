package evaluator

import nodes.Program
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.statements.*
import kotlin.system.exitProcess

class Evaluator (program: Program) {

    private val program: Program
    private val env: Environment

    init {
        this.program = program
        this.env = Environment()
    }

    fun run() {
        for (stmt in program.getStatements()) {
            executeStatement(stmt)
        }
    }

    private fun executeStatement(statement: Statement) {
        when (statement.getType()) {
            StatementType.VAR_DEC_STMT -> executeVarDecStmt(statement as VarDec)
            StatementType.VAR_ASSIGN_STMT -> executeVarAssignStmt(statement as VarAssign)
            StatementType.WHILE_STMT -> executeWhileStmt(statement as While)
            StatementType.REPEAT_STMT -> executeRepeatStmt(statement as Repeat)
            StatementType.RETURN_STMT -> executeReturnStmt(statement as Return)
            StatementType.FUNC_CALL_STMT -> executeFuncCallStmt(statement as FuncCall)
            StatementType.FUNC_DEF_STMT -> executeFuncDefStmtStmt(statement as FuncDef)
        }
    }

    private fun executeVarDecStmt(varDec: VarDec) {
        val varName: String = varDec.getVarName()
        val (value, type) = varDec.getValue().eval(env)
        env.declareVariable(varName, value, type)
    }

    private fun executeVarAssignStmt(varAssign: VarAssign) {
        val varName: String = varAssign.getVarName()
        val (value, type) = varAssign.getValue().eval(env)
        env.updateVariable(varName, value, type)
    }

    private fun executeWhileStmt(whileStmt: While) {
        var pair = whileStmt.getCondition().eval(env)
        val body = whileStmt.getBody()

        if (pair.second != ValueType.BOOLEAN) {
            println("While stmt condition did not resolve to type Boolean")
            exitProcess(0)
        }

        // TODO implement Variable expression class
        while (pair.first as Boolean) {
            for (stmt in body) {
                executeStatement(stmt)
            }
            pair = whileStmt.getCondition().eval(env)
            if (pair.second != ValueType.BOOLEAN) {
                println("While stmt condition did not resolve to type Boolean")
                exitProcess(0)
            }
        }
    }

    private fun executeRepeatStmt(repeatStmt: Repeat) {
        val pair = repeatStmt.getCondition().eval(env)
        val body = repeatStmt.getBody()

        if (pair.second != ValueType.INTEGER) {
            println("While stmt condition did not resolve to type Boolean")
            exitProcess(0)
        }

        repeat (pair.first as Int) {
            for (stmt in body) {
                executeStatement(stmt)
            }
        }
    }

    private fun executeFuncDefStmtStmt(funcDefStmt: FuncDef) {

    }

    private fun executeFuncCallStmt(funcCallStmt: FuncCall) {
        if (funcCallStmt.getFunctionName() == "print" && funcCallStmt.getNumberOfArguments() == 1) {
            val (value, type) = funcCallStmt.getArguments()[0].eval(env)

            println(value)
        }
    }

    private fun executeReturnStmt(returnStmt: Return) {

    }
}