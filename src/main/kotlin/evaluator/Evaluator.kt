package evaluator

import nodes.Program
import nodes.expression_and_statement.ConstructorCall
import nodes.expression_and_statement.FuncCall
import nodes.expression_and_statement.MethodCall
import nodes.interfaces.Statement
import nodes.interfaces.StatementType
import nodes.statements.*
import runtime.Runtime
import standard_lib.objects.ListRF
import standard_lib.objects.Object
import standard_lib.objects.ObjectType

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

        private fun executeStatement(statement: Statement) {
            when (statement.getType()) {
                StatementType.VAR_DEC_STMT -> executeVarDecStmt(statement as VarDec)
                StatementType.VAR_ASSIGN_STMT -> executeVarAssignStmt(statement as VarAssign)
                StatementType.WHILE_STMT -> executeWhileStmt(statement as While)
                StatementType.REPEAT_STMT -> executeRepeatStmt(statement as Repeat)
                StatementType.RETURN_STMT -> executeReturnStmt(statement as Return)
                StatementType.FUNC_CALL_STMT -> executeFuncCall(statement as FuncCall)
                StatementType.FUNC_DEF_STMT -> executeFuncDefStmtStmt(statement as FuncDef)
                StatementType.METHOD_CALL_STMT -> executeMethodCall(statement as MethodCall)
                StatementType.IF_STMT -> executeIfStatement(statement as If)
                StatementType.CONSTRUCTOR_CALL_STMT -> executeConstructorCall(statement as ConstructorCall)
                StatementType.FOR_IN_STMT -> executeForInStmt(statement as ForIn)
                else -> {}
            }
        }

        private fun executeVarDecStmt(varDec: VarDec) {
            val varName: String = varDec.getVarName()
            val value = varDec.getValue().eval()
            EnvironmentManager.declareVariable(varName, value.getValue(), value.getType())
        }

        private fun executeForInStmt(forIn: ForIn): Pair<Value, Boolean> {

            val value = forIn.getExpression().eval()

            when (value.getType()) {
                ValueType.STRING -> {
                    EnvironmentManager.pushFunctionEnvironment()
                    EnvironmentManager.declareVariable(forIn.getLocalVar(), "", ValueType.STRING)
                    for (char in value.getValue() as String) {
                        EnvironmentManager.updateVariable(forIn.getLocalVar(), char.toString(), ValueType.STRING)
                        for (stmt in forIn.getStmts()) {
                            executeStatement(stmt)
                        }
                    }
                    EnvironmentManager.popFunctionEnvironment()
                }
                ValueType.OBJECT -> {
                    var obj = value.getValue() as Object
                    if (obj.name() != "List") {
                        Runtime.raiseError("Expression must resolve to type List in For In")
                    }

                    obj = obj as ListRF
                    if (obj.isEmptyKotlin()) { return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false) }
                    val list = obj.getListKotlin()

                    EnvironmentManager.pushFunctionEnvironment()
                    EnvironmentManager.declareVariable(forIn.getLocalVar(), list[0], obj.getType())
                    for (element in list) {
                        EnvironmentManager.updateVariable(forIn.getLocalVar(), element, obj.getType())
                        for (stmt in forIn.getStmts()) {
                            when (stmt.getType()) {
                                StatementType.BREAK_STMT -> {
                                    EnvironmentManager.popFunctionEnvironment()
                                    return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
                                }
                                StatementType.IF_STMT -> {
                                    val (v, b) = executeIfStatement(stmt as If)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.WHILE_STMT -> {
                                    val (v, b) = executeWhileStmt(stmt as While)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.REPEAT_STMT -> {
                                    val (v, b) = executeRepeatStmt(stmt as Repeat)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.FOR_IN_STMT -> {
                                    val (v, b) = executeForInStmt(stmt as ForIn)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                                else -> executeStatement(stmt)
                            }
                        }
                    }
                    EnvironmentManager.popFunctionEnvironment()
                }
                else -> Runtime.raiseError("For In only works on Strings and Iterable Objects")
            }
            return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
        }

        private fun executeVarAssignStmt(varAssign: VarAssign) {
            val varName: String = varAssign.getVarName()
            val value = varAssign.getValue().eval()
            EnvironmentManager.updateVariable(varName, value.getValue(), value.getType())
        }

        private fun executeWhileStmt(whileStmt: While): Pair<Value, Boolean> {
            var value = whileStmt.getCondition().eval()
            val body = whileStmt.getBody()

            if (value.getType() != ValueType.BOOLEAN) {
                Runtime.raiseError("While stmt condition did not resolve to type Boolean")
            }

            outer@ while (value.getValue() as Boolean) {
                for (stmt in body) {
                    when (stmt.getType()) {
                        StatementType.BREAK_STMT -> break@outer
                        StatementType.IF_STMT -> {
                            val (v, b) = executeIfStatement(stmt as If)
                            if (b) return Pair(v, b)
                        }
                        StatementType.WHILE_STMT -> {
                            val (v, b) = executeWhileStmt(stmt as While)
                            if (b) return Pair(v, b)
                        }
                        StatementType.REPEAT_STMT -> {
                            val (v, b) = executeRepeatStmt(stmt as Repeat)
                            if (b) return Pair(v, b)
                        }
                        StatementType.FOR_IN_STMT -> {
                            val (v, b) = executeForInStmt(stmt as ForIn)
                            if (b) return Pair(v, b)
                        }
                        StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                        else -> executeStatement(stmt)
                    }
                }
                value = whileStmt.getCondition().eval()
                if (value.getType() != ValueType.BOOLEAN) {
                    Runtime.raiseError("While stmt condition did not resolve to type Boolean")
                }
            }
            return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
        }

        private fun executeRepeatStmt(repeatStmt: Repeat): Pair<Value, Boolean> {
            val value = repeatStmt.getCondition().eval()
            val body = repeatStmt.getBody()

            if (value.getType() != ValueType.INTEGER) {
                Runtime.raiseError("While stmt condition did not resolve to type Boolean")
            }

            run repeatBlock@ {
                repeat (value.getValue() as Int) {
                    for (stmt in body) {
                        when (stmt.getType()) {
                            StatementType.BREAK_STMT -> return@repeatBlock
                            StatementType.IF_STMT -> {
                                val (v, b) = executeIfStatement(stmt as If)
                                if (b) return Pair(v, b)
                            }
                            StatementType.WHILE_STMT -> {
                                val (v, b) = executeWhileStmt(stmt as While)
                                if (b) return Pair(v, b)
                            }
                            StatementType.REPEAT_STMT -> {
                                val (v, b) = executeRepeatStmt(stmt as Repeat)
                                if (b) return Pair(v, b)
                            }
                            StatementType.FOR_IN_STMT -> {
                                val (v, b) = executeForInStmt(stmt as ForIn)
                                if (b) return Pair(v, b)
                            }
                            StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                            else -> executeStatement(stmt)
                        }
                    }
                }
            }
            return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
        }

        fun executeConstructorCall(constructorCall: ConstructorCall): Value {
            val value = EnvironmentManager.createObject(constructorCall.getConstructorName())

            // Special case
            if ((value.getValue() as Object).name() == "List") {
                val list = value.getValue() as ListRF
                for (expr in constructorCall.getExpressions()) {
                    list.add(expr.eval())
                }
            }

            if (constructorCall.hasChainedMethodCalls()) {
                return executeChainedMethodCall(value, constructorCall.getChainedMethodCalls())
            }

            return Value(value.getValue(), value.getType())
        }

        fun executeMethodCall(methodCall: MethodCall): Value {
            val value = EnvironmentManager.getVariable(methodCall.getObjectName())
            val arguments = methodCall.getArguments()

            if (value.getType() != ValueType.OBJECT) {
                Runtime.raiseError("${value.getType()} does not support method calls")
            }

            val obj = value.getValue() as Object
            when (obj.type()) {
                ObjectType.USER_DEFINED -> Runtime.raiseError("User defined objects not yet implemented")
                ObjectType.STANDARD_LIB -> {
                    return if (methodCall.isChainedMethodCall()) {
                        val firstCall = obj.callMethod(methodCall.getMethodName(), arguments)
                        executeChainedMethodCall(firstCall, methodCall.getChainedMethodCalls())
                    } else {
                        obj.callMethod(methodCall.getMethodName(), arguments)
                    }
                }
            }
        }

        private fun executeChainedMethodCall(value: Value, remainingMethodCalls: List<MethodCall>): Value {
            var lastValue = value
            remainingMethodCalls.forEachIndexed { index, methodCall ->
                when (lastValue.getType()) {
                    ValueType.OBJECT -> {
                        lastValue = (lastValue.getValue() as Object)
                            .callMethod(methodCall.getMethodName(), methodCall.getArguments())

                        if (index == remainingMethodCalls.size - 1) {
                            return lastValue
                        }
                    }
                    else -> Runtime.raiseError("Cannot call method on non-object type")
                }
            }
            return Value(Value.Companion.NULL(), ValueType.NULL)
        }

        private fun executeFuncDefStmtStmt(funcDefStmt: FuncDef) {
            EnvironmentManager.declareFunction(
                funcDefStmt.getFunctionName(),
                funcDefStmt.getParams(),
                funcDefStmt.getBody()
            )
        }

        fun executeFuncCall(funcCall: FuncCall): Value {
            val functionName = funcCall.getFunctionName()
            val arguments = funcCall.getArguments()

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

                    // eval each expression in arguments first because
                    // it may rely on the old function environment (recursive functions)
                    val resolved_arguments = arguments.map { it.eval() }

                    EnvironmentManager.pushFunctionEnvironment()
                    resolved_arguments.forEachIndexed { index, value ->
                        EnvironmentManager.declareVariable(
                            functionParams[index],
                            value.getValue(),
                            value.getType()
                        )
                    }

                    for (stmt in functionBody) {
                        when (stmt.getType()) {
                            StatementType.IF_STMT -> {
                                val (v, b) = executeIfStatement(stmt as If)
                                if (b) return v
                            }
                            StatementType.WHILE_STMT -> {
                                val (v, b) = executeWhileStmt(stmt as While)
                                if (b) return v
                            }
                            StatementType.REPEAT_STMT -> {
                                val (v, b) = executeRepeatStmt(stmt as Repeat)
                                if (b) return v
                            }
                            StatementType.FOR_IN_STMT -> {
                                val (v, b) = executeForInStmt(stmt as ForIn)
                                if (b) return v
                            }
                            StatementType.RETURN_STMT -> return executeReturnStmt(stmt as Return)
                            StatementType.END_STMT -> {
                                EnvironmentManager.popFunctionEnvironment()
                                return Value(Value.Companion.NULL(), ValueType.NULL)
                            }
                            else -> executeStatement(stmt)
                        }
                    }
                    EnvironmentManager.popFunctionEnvironment()
                    return Value(Value.Companion.NULL(), ValueType.NULL)
                }

                FunctionType.STANDARD_LIB -> {
                    function = function as StandardLibFunction
                    val functionParams = function.getParams()
                    if (arguments.size != functionParams.size) {
                        Runtime.raiseError(
                            "Function '$functionName' expected ${functionParams.size} argument(s) but received" +
                                    " ${arguments.size} argument(s) instead")
                    }
                    return function.run(arguments)
                }
            }
        }

        private fun executeReturnStmt(returnStmt: Return): Value {
            if (EnvironmentManager.isFunctionEnvironmentEmpty()) {
                Runtime.raiseError("Cannot return from outside function body")
            }

            val returnValue = returnStmt.getExpression().eval()
            EnvironmentManager.popFunctionEnvironment()
            return returnValue
        }

        private fun executeIfStatement(ifStmt: If): Pair<Value, Boolean> {
            val value = ifStmt.getIfCondition().eval()
            if (value.getType() != ValueType.BOOLEAN) {
                Runtime.raiseError("if <expr> must resolve to type Boolean")
            }

            if (value.getValue() as Boolean) {
                for (stmt in ifStmt.getIfStmts()) {
                    when (stmt.getType()) {
                        StatementType.IF_STMT -> {
                            val (v, b) = executeIfStatement(stmt as If)
                            if (b) return Pair(v, b)
                        }
                        StatementType.WHILE_STMT -> {
                            val (v, b) = executeWhileStmt(stmt as While)
                            if (b) return Pair(v, b)
                        }
                        StatementType.REPEAT_STMT -> {
                            val (v, b) = executeRepeatStmt(stmt as Repeat)
                            if (b) return Pair(v, b)
                        }
                        StatementType.FOR_IN_STMT -> {
                            val (v, b) = executeForInStmt(stmt as ForIn)
                            if (b) return Pair(v, b)
                        }
                        StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                        else -> executeStatement(stmt)
                    }
                }
            } else {
                for (elif in ifStmt.getElseIfList()) {
                    val elifValue = elif.getCondition().eval()
                    if (elifValue.getType() != ValueType.BOOLEAN) {
                        Runtime.raiseError("elif <expr> must resolve to type Boolean")
                    }

                    if (elifValue.getValue() as Boolean) {
                        for (stmt in elif.getStmts()) {
                            when (stmt.getType()) {
                                StatementType.IF_STMT -> {
                                    val (v, b) = executeIfStatement(stmt as If)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.WHILE_STMT -> {
                                    val (v, b) = executeWhileStmt(stmt as While)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.REPEAT_STMT -> {
                                    val (v, b) = executeRepeatStmt(stmt as Repeat)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.FOR_IN_STMT -> {
                                    val (v, b) = executeForInStmt(stmt as ForIn)
                                    if (b) return Pair(v, b)
                                }
                                StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                                else -> executeStatement(stmt)
                            }
                        }
                        return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
                    }
                }
                for (stmt in ifStmt.getElseStmts()) {
                    when (stmt.getType()) {
                        StatementType.IF_STMT -> {
                            val (v, b) = executeIfStatement(stmt as If)
                            if (b) return Pair(v, b)
                        }
                        StatementType.WHILE_STMT -> {
                            val (v, b) = executeWhileStmt(stmt as While)
                            if (b) return Pair(v, b)
                        }
                        StatementType.REPEAT_STMT -> {
                            val (v, b) = executeRepeatStmt(stmt as Repeat)
                            if (b) return Pair(v, b)
                        }
                        StatementType.FOR_IN_STMT -> {
                            val (v, b) = executeForInStmt(stmt as ForIn)
                            if (b) return Pair(v, b)
                        }
                        StatementType.RETURN_STMT -> return Pair(executeReturnStmt(stmt as Return), true)
                        else -> executeStatement(stmt)
                    }
                }
            }
            return Pair(Value(Value.Companion.NULL(), ValueType.NULL), false)
        }
    }
}