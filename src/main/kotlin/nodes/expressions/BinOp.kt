package nodes.expressions

import evaluator.Value
import evaluator.ValueType
import nodes.interfaces.Expression
import nodes.root.Node
import runtime.Runtime
import tokens.TokenType

class BinOp (lhs: Expression, operator: TokenType, rhs: Expression): Node(), Expression {

    private val lhs: Expression
    private val rhs: Expression
    private val operator: TokenType

    init {
        this.lhs = lhs
        this.rhs = rhs
        this.operator = operator
    }

    override fun eval(): Value {
        val lhsValue = lhs.eval()
        val rhsValue = rhs.eval()
        val lhs_value = lhsValue.getValue()
        val rhs_value = rhsValue.getValue()
        val lhs_type = lhsValue.getType()
        val rhs_type = rhsValue.getType()

        when (operator) {
            TokenType.EQ_EQ -> {
                if (lhs_type != rhs_type) {
                    Runtime.raiseError("types must be equal in EQ EQ BinOp")
                }

                when (lhs_type) {
                    ValueType.INTEGER -> {
                        val equal = (lhs_value as Int) == (rhs_value as Int)
                        return Value(equal, ValueType.BOOLEAN)
                    }
                    ValueType.STRING -> {
                        val equal = (lhs_value as String) == (rhs_value as String)
                        return Value(equal, ValueType.BOOLEAN)
                    }
                    ValueType.BOOLEAN -> {
                        val equal = (lhs_value as Boolean) == (rhs_value as Boolean)
                        return Value(equal, ValueType.BOOLEAN)
                    }
                    ValueType.NULL -> {
                        Runtime.raiseError("null reference in binop expression")
                    }
                    ValueType.OBJECT -> throw NotImplementedError("EQ_EQ for Objects")
                }

            }
            TokenType.BANG_EQ -> {
                if (lhs_type != rhs_type) {
                    Runtime.raiseError("types must be equal in EQ EQ BinOp")
                }

                when (lhs_type) {
                    ValueType.INTEGER -> {
                        val notEqual = (lhs_value as Int) != (rhs_value as Int)
                        return Value(notEqual, ValueType.BOOLEAN)
                    }
                    ValueType.STRING -> {
                        val notEqual = (lhs_value as String) != (rhs_value as String)
                        return Value(notEqual, ValueType.BOOLEAN)
                    }
                    ValueType.BOOLEAN -> {
                        val notEqual = (lhs_value as Boolean) != (rhs_value as Boolean)
                        return Value(notEqual, ValueType.BOOLEAN)
                    }
                    ValueType.NULL -> {
                        Runtime.raiseError("null reference in binop expression")
                    }
                    ValueType.OBJECT -> throw NotImplementedError("BANG_EQ for Objects")
                }
            }
            TokenType.LT -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for LT")
                }
                val result = (lhs_value as Int) < (rhs_value as Int)
                return Value(result, ValueType.BOOLEAN)
            }
            TokenType.LT_EQ -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for LT EQ")
                }
                val result = (lhs_value as Int) <= (rhs_value as Int)
                return Value(result, ValueType.BOOLEAN)
            }
            TokenType.GT -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for GT")
                }
                val result = (lhs_value as Int) > (rhs_value as Int)
                return Value(result, ValueType.BOOLEAN)
            }
            TokenType.GT_EQ -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for GT EQ")
                }
                val result = (lhs_value as Int) >= (rhs_value as Int)
                return Value(result, ValueType.BOOLEAN)
            }
            TokenType.PLUS -> {
                return when (lhs_type) {
                    ValueType.INTEGER -> {
                        when (rhs_type) {
                            ValueType.INTEGER -> {
                                val result = (lhs_value as Int) + (rhs_value as Int)
                                return Value(result, ValueType.INTEGER)
                            }
                            ValueType.STRING -> {
                                val result = (rhs_value as String).plus(lhs_value as Int)
                                return Value(result, ValueType.STRING)
                            }
                            else -> Runtime.raiseError("cannot add $lhs_type with $rhs_type")
                        }

                    }
                    ValueType.STRING -> {
                        when (rhs_type) {
                            ValueType.INTEGER -> {
                                val result = (lhs_value as String).plus(rhs_value as Int)
                                return Value(result, ValueType.STRING)
                            }
                            ValueType.STRING -> {
                                val result = (lhs_value as String).plus(rhs_value as String)
                                return Value(result, ValueType.STRING)
                            }
                            else -> Runtime.raiseError("cannot add $lhs_type with $rhs_type")
                        }
                    }
                    else -> {
                        Runtime.raiseError("addition can only occur on integers and strings")
                    }
                }
            }
            TokenType.MINUS -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for subtraction")
                }
                val result = (lhs_value as Int) - (rhs_value as Int)
                return Value(result, ValueType.INTEGER)
            }
            TokenType.MULTIPLY -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for multiplication")
                }
                val result = (lhs_value as Int) * (rhs_value as Int)
                return Value(result, ValueType.INTEGER)
            }
            TokenType.DIVIDE -> {
                if (lhs_type != ValueType.INTEGER || rhs_type != ValueType.INTEGER) {
                    Runtime.raiseError("expected integer types for division")
                }
                val result = (lhs_value as Int) / (rhs_value as Int)
                return Value(result, ValueType.INTEGER)
            }
            TokenType.KEYWORD_AND -> {
                if (lhs_type != ValueType.BOOLEAN || rhs_type != ValueType.BOOLEAN) {
                    Runtime.raiseError("expected boolean types for AND operation")
                }
                val result = (lhs_value as Boolean) && (rhs_value as Boolean)
                return Value(result, ValueType.BOOLEAN)
            }
            TokenType.KEYWORD_OR -> {
                if (lhs_type != ValueType.BOOLEAN || rhs_type != ValueType.BOOLEAN) {
                    Runtime.raiseError("expected boolean types for OR operation")
                }
                val result = (lhs_value as Boolean) || (rhs_value as Boolean)
                return Value(result, ValueType.BOOLEAN)
            }
            else -> {
                Runtime.raiseError("invalid BinOp operator")
            }
        }
    }

    override fun toString(): String {
        return "BinOp"
    }
}