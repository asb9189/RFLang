package evaluator

import nodes.Program
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.statements.*
import runtime.Runtime
import kotlin.system.exitProcess

class Evaluator {

    companion object {

        fun run(program: Program) {

            val funcDefStmts = mutableListOf<Statement>()
            val otherStmts = mutableListOf<Statement>()

            for (stmt in program.getStatements()) {
                when (stmt.getType()) {
                    StatementType.FUNC_DEF_STMT -> funcDefStmts.add(stmt)
                    else -> otherStmts.add(stmt)
                }
            }

            for (stmt in funcDefStmts) {
                executeStatement(stmt)
            }

            for (stmt in otherStmts) {
                executeStatement(stmt)
            }
        }

        fun executeStatement(statement: Statement) {
            when (statement.getType()) {
                StatementType.VAR_DEC_STMT -> executeVarDecStmt(statement as VarDec)
                StatementType.VAR_ASSIGN_STMT -> executeVarAssignStmt(statement as VarAssign)
                StatementType.WHILE_STMT -> executeWhileStmt(statement as While)
                StatementType.REPEAT_STMT -> executeRepeatStmt(statement as Repeat)
                StatementType.RETURN_STMT -> executeReturnStmt(statement as Return)
                StatementType.FUNC_CALL_STMT -> executeFuncCallStmt(statement as FuncCallStmt)
                StatementType.FUNC_DEF_STMT -> executeFuncDefStmtStmt(statement as FuncDef)
                StatementType.IF_STMT -> executeIfStatement(statement as If)
            }
        }

        private fun executeVarDecStmt(varDec: VarDec) {
            val varName: String = varDec.getVarName()
            val (value, type) = varDec.getValue().eval()
            EnvironmentManager.declareVariable(varName, value, type)
        }

        private fun executeVarAssignStmt(varAssign: VarAssign) {
            val varName: String = varAssign.getVarName()
            val (value, type) = varAssign.getValue().eval()
            EnvironmentManager.updateVariable(varName, value, type)
        }

        private fun executeWhileStmt(whileStmt: While) {
            var pair = whileStmt.getCondition().eval()
            val body = whileStmt.getBody()

            if (pair.second != ValueType.BOOLEAN) {
                println("While stmt condition did not resolve to type Boolean")
                exitProcess(0)
            }

            while (pair.first as Boolean) {
                for (stmt in body) {
                    executeStatement(stmt)
                }
                pair = whileStmt.getCondition().eval()
                if (pair.second != ValueType.BOOLEAN) {
                    println("While stmt condition did not resolve to type Boolean")
                    exitProcess(0)
                }
            }
        }

        private fun executeRepeatStmt(repeatStmt: Repeat) {
            val pair = repeatStmt.getCondition().eval()
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
            EnvironmentManager.declareFunction(
                funcDefStmt.getFunctionName(),
                funcDefStmt.getParams(),
                funcDefStmt.getBody()
            )
        }

        private fun executeFuncCallStmt(funcCallStmt: FuncCallStmt) {
            val functionName = funcCallStmt.getFunctionName()
            val arguments = funcCallStmt.getArguments()

            var function = EnvironmentManager.getFunction(functionName)
            when (function.getFunctionType()) {
                FunctionType.USER_DEFINED -> {
                    function = function as UserDefinedFunction
                    val functionParams = function.getParams()
                    val functionBody = function.getBody()

                    if (arguments.size != functionParams.size) {
                        Runtime.raiseError(
                            "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                                    " ${arguments.size} argument(s) instead")
                    }

                    EnvironmentManager.pushFunctionEnvironment()
                    arguments.forEachIndexed { index, expression ->
                        val (value, type) = expression.eval()
                        EnvironmentManager.declareVariable(
                            functionParams[index],
                            value,
                            type
                        )
                    }

                    for (stmt in functionBody) {
                        if (stmt.getType() == StatementType.RETURN_STMT) {
                            EnvironmentManager.popFunctionEnvironment()
                            return
                        }
                        executeStatement(stmt)
                    }
                    EnvironmentManager.popFunctionEnvironment()
                }
                FunctionType.STANDARD_LIB -> {
                    function = function as StandardLibFunction
                    val functionParams = function.getParams()
                    if (arguments.size != functionParams.size) {
                        Runtime.raiseError(
                            "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                                    " ${arguments.size} argument(s) instead")
                    }
                    function.run(arguments)
                }
            }
        }

        fun executeReturnStmt(returnStmt: Return): Pair<Any, ValueType> {
            if (EnvironmentManager.isFunctionEnvironmentEmpty()) {
                exitProcess(0)
            }
            return returnStmt.getExpression().eval()
        }

        private fun executeIfStatement(ifStmt: If) {
            val (value, type) = ifStmt.getIfCondition().eval()
            if (type != ValueType.BOOLEAN) {
                Runtime.raiseError("if <expr> must resolve to type Boolean")
            }

            if (value as Boolean) {
                for (stmt in ifStmt.getIfStmts()) {
                    executeStatement(stmt)
                }
            } else {
                for (elif in ifStmt.getElseIfList()) {
                    val (elifVal, elifValType) = elif.getCondition().eval()
                    if (elifValType != ValueType.BOOLEAN) {
                        Runtime.raiseError("elif <expr> must resolve to type Boolean")
                    }

                    if (elifVal as Boolean) {
                        for (stmt in elif.getStmts()) {
                            executeStatement(stmt)
                        }
                        return
                    }
                }
                for (stmt in ifStmt.getElseStmts()) {
                    executeStatement(stmt)
                }
            }
        }
    }
}