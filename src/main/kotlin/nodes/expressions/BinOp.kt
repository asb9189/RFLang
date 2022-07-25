package nodes.expressions

import evaluator.Environment
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import tokens.TokenType
import kotlin.system.exitProcess


class BinOp (lhs: Expression, operator: TokenType, rhs: Expression): Node(), Expression {

    private val lhs: Expression
    private val rhs: Expression
    private val operator: TokenType

    init {
        this.lhs = lhs
        this.rhs = rhs
        this.operator = operator
    }

    override fun eval(): Pair<Any, ValueType> {
        val (lhs_value, lhs_type) = lhs.eval()
        val (rhs_value, rhs_type) = rhs.eval()

        return when (operator) {
            TokenType.EQ_EQ -> {
                if (lhs_type != rhs_type) {
                    println("types must be equal in EQ EQ BinOp")
                    exitProcess(0)
                }

                return when (lhs_type) {
                    ValueType.INTEGER -> {
                        val equal = (lhs_value as Int) == (rhs_value as Int)
                        Pair(equal, ValueType.BOOLEAN)
                    }
                    ValueType.STRING -> {
                        val equal = (lhs_value as String) == (rhs_value as String)
                        Pair(equal, ValueType.BOOLEAN)
                    }
                    ValueType.BOOLEAN -> {
                        val equal = (lhs_value as Boolean) == (rhs_value as Boolean)
                        Pair(equal, ValueType.BOOLEAN)
                    }
                }

            }
            TokenType.BANG_EQ -> {
                if (lhs_type != rhs_type) {
                    println("types must be equal in EQ EQ BinOp")
                    exitProcess(0)
                }

                return when (lhs_type) {
                    ValueType.INTEGER -> {
                        val notEqual = (lhs_value as Int) != (rhs_value as Int)
                        Pair(notEqual, ValueType.BOOLEAN)
                    }
                    ValueType.STRING -> {
                        val notEqual = (lhs_value as String) != (rhs_value as String)
                        Pair(notEqual, ValueType.BOOLEAN)
                    }
                    ValueType.BOOLEAN -> {
                        val notEqual = (lhs_value as Boolean) != (rhs_value as Boolean)
                        Pair(notEqual, ValueType.BOOLEAN)
                    }
                }
            }
            TokenType.LT -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for LT")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) < (rhs_value as Int)
                Pair(result, ValueType.BOOLEAN)
            }
            TokenType.LT_EQ -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for LT EQ")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) <= (rhs_value as Int)
                Pair(result, ValueType.BOOLEAN)
            }
            TokenType.GT -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for GT")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) > (rhs_value as Int)
                Pair(result, ValueType.BOOLEAN)
            }
            TokenType.GT_EQ -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for GT EQ")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) >= (rhs_value as Int)
                Pair(result, ValueType.BOOLEAN)
            }
            TokenType.PLUS -> {
                if (lhs_type != rhs_type) {
                    println("types must be equal for addition")
                    exitProcess(0)
                }

                when (lhs_type) {
                    ValueType.INTEGER -> {
                        val result = (lhs_value as Int) + (rhs_value as Int)
                        Pair(result, ValueType.INTEGER)
                    }
                    ValueType.STRING -> {
                        val result = (lhs_value as String) + (rhs_value as String)
                        Pair(result, ValueType.STRING)
                    }
                    else -> {
                        println("addition can only occur on integers and strings")
                        exitProcess(0)
                    }
                }
            }
            TokenType.MINUS -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for subtraction")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) - (rhs_value as Int)
                Pair(result, ValueType.INTEGER)
            }
            TokenType.MULTIPLY -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for multiplication")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) * (rhs_value as Int)
                Pair(result, ValueType.INTEGER)
            }
            TokenType.DIVIDE -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    println("expected integer types for division")
                    exitProcess(0)
                }
                val result = (lhs_value as Int) / (rhs_value as Int)
                Pair(result, ValueType.INTEGER)
            }
            TokenType.KEYWORD_AND -> {
                if (lhs_type != ValueType.BOOLEAN || rhs_type != ValueType.BOOLEAN) {
                    println("expected boolean types for AND operation")
                    exitProcess(0)
                }
                val result = (lhs_value as Boolean) && (rhs_value as Boolean)
                Pair(result, ValueType.BOOLEAN)
            }
            TokenType.KEYWORD_OR -> {
                if (lhs_type != ValueType.BOOLEAN || rhs_type != ValueType.BOOLEAN) {
                    println("expected boolean types for OR operation")
                    exitProcess(0)
                }
                val result = (lhs_value as Boolean) || (rhs_value as Boolean)
                Pair(result, ValueType.BOOLEAN)
            }

            else -> {
                println("invalid BinOp operator")
                exitProcess(0)
            }
        }
    }

    override fun toString(): String {
        return "BinOp"
    }
}