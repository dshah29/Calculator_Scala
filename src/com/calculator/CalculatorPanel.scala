package com.calculator

import javax.swing._
import java.awt._

class CalculatorPanel extends JPanel 
{
  private var row: Int = 0
  private var column: Int = -1

  setLayout(new GridBagLayout)
  def atNewLine(component: JComponent): CalculatorPanel = atNewLineWithColumnSpan(component, 1)

  def atNewLineWithColumnSpan(component: JComponent, width: Int): CalculatorPanel = 
  {
    row += 1
    column = 0
    add(component, constraints(width, 1))
    column += width - 1
    this
  }

  def beside(component: JComponent): CalculatorPanel = besideWithRowSpan(component, 1)

  def besideWithRowSpan(component: JComponent, height: Int): CalculatorPanel = 
  {
    column += 1
    add(component, constraints(1, height))
    this
  }

  private def constraints(width: Int, height: Int): GridBagConstraints = new GridBagConstraints 
  {
    gridx = column
    gridy = row
    gridwidth = width
    gridheight = height
    insets = new Insets(2, 2, 2, 4)
    fill = GridBagConstraints.BOTH
  }
}