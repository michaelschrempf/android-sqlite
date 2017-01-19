package net.ladstatt.android.sqlite

import scala.util.Random

/**
  * Created by lad on 19/01/2017.
  */
object Gen {

  val lower = 'a' to 'z'
  val upper = 'A' to 'Z'

  def aLower: Char = lower(Random.nextInt(lower.length))

  def aUpper: Char = upper(Random.nextInt(upper.length))

  def aword: String = List.fill(Random.nextInt(15))(aLower).mkString("")

  def Aword: String = aUpper + aword

}