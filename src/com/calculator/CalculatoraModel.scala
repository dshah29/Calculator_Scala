package com.calculator

import scala.math.BigDecimal
import java.math.MathContext
import annotation.tailrec
import scala.math.sqrt



class CalculatorModel 
{
  private val precision = 16
  private val maxValue = BigDecimal("9999999999999999")
  private val minValue = BigDecimal("0.0000000000000001")
  private var prevValue: String = _
  private var currValue: String = _
  private var operation: String = _
  private var isNewValue: Boolean = _
  
  reset()

  def getCurrValue: String = currValue
  
  def pressButton(name: String): Unit = name match
  {
      case "C" => reset()
      case "=" => completeOperation()
      case "+" | "-" | "*" | "/" => setOperation(name)
      case "Mod" | "x^y" => setOperation(name)
      case "tan" | "sin" | "cos" | "tanh" | "sinh" | "cosh" => { setOperation(name) ; completeOperation() }
      case "x^2" | "x^3" |  "log" | "1/x" | "10^x" | "sqrt(x)" => { setOperation(name) ; completeOperation() }
      case "Pi" => {currValue = Math.PI.toString(); setOperation("=")}
      case _ => appendDigit(name)
    }

  private def reset(): Unit = 
  {
    currValue = "0"
    setOperation("=")
  }

  private def appendDigit(digit: String): Unit = 
  {
    val v = if (isNewValue && digit != ".") "" 
            else currValue
    if (isDuplicatedDot(v, digit) || isLengthMaximal(v)) throw new IllegalArgumentException
    currValue = dropLeftZeros(v + digit)
    isNewValue = false
  }

  private def setOperation(op: String): Unit = 
  {
    operation = op
    prevValue = currValue
    isNewValue = true
  }

  private def completeOperation(): Unit =
  {
    try 
    {
      currValue = dropRightZeros(round(evaluate()).toString)
    }
    catch 
    {
      case ex: RuntimeException => currValue = "Error"
    }
    isNewValue = true
  }

  private def evaluate(): BigDecimal = 
  {
    val x = BigDecimal(prevValue)
    val y = BigDecimal(currValue)
    val z = BigDecimal(10)
    operation match 
    {
      case "+" => x + y
      case "-" => x - y
      case "*" => x * y
      case "/" => x / y
      case "tan" => BigDecimal.double2bigDecimal(Math.tan(y.toDouble))
      case "sin" => BigDecimal.double2bigDecimal(Math.sin(y.toDouble))
      case "cos" => BigDecimal.double2bigDecimal(Math.cos(y.toDouble))
      case "tanh" => BigDecimal.double2bigDecimal(Math.tanh(y.toDouble))
      case "sinh" => BigDecimal.double2bigDecimal(Math.sinh(y.toDouble))
      case "cosh" => BigDecimal.double2bigDecimal(Math.cosh(y.toDouble))
      case "Mod" => x % y
      case "x^y" => x.pow(y.intValue())
      case "x^2" => x.pow(2)
      case "x^3" => x.pow(3)
      case "1/x" => 1/x
      case "10^x" => z.pow(x.intValue())
      case "sqrt(x)" => sqrt(x.toDouble)
      case "log" => BigDecimal.double2bigDecimal(Math.log(y.toDouble))
      case _ => y
    }
  }

  private def round(v: BigDecimal): BigDecimal = v.abs match 
  {
    case va if va > maxValue => throw new IllegalArgumentException
    case va if va < minValue => BigDecimal(0)
    case _ => v.round(new MathContext(precision))
  }

  private def isLengthMaximal(v: String): Boolean = v.replace(".", "").length == precision

  private def isDuplicatedDot(v: String, digit: String): Boolean = v.contains(".") && digit == "."

  
  private def dropRightZeros(v: String): String =
    if (v.contains(".") && (v.endsWith("0") || v.endsWith("."))) dropRightZeros(v.dropRight(1)) 
     else v

 
  private def dropLeftZeros(v: String): String =
    if (v.startsWith("0") && v.length > 1 && !v.startsWith("0.")) dropLeftZeros(v.drop(1)) 
      else v
}