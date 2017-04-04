package org.quantlib.process

import org.scalatest._

/**
  * Created by neo on 26/02/2017.
  */
class StochasticProcess1DSpec extends FlatSpec with Matchers {
  val tolerance = 1E-10

  val times1 = Seq.empty[Double]
  val vols1 = Seq(1.0)

  val times2 = Seq(1.0, 2.0)
  val vols2 = Seq(1.0, 2.0, 3.0)

  val sp1 = MarkovFunctionalStateProcess(0.00, times1, vols1)
  val sp2 = MarkovFunctionalStateProcess(0.00, times2, vols2)
  val sp3 = MarkovFunctionalStateProcess(0.01, times2, vols2)

  "MarkovFunctionalStateProcess sp1 variance" should "eventually compute the variance" in {

    val var11 = sp1.variance(0.0, 0.0, 1.0)
    val var12 = sp1.variance(0.0, 0.0, 2.0)

    assert(var11 == 1.0, s"process 1 has not variance 1.0 for dt = 1.0 but $var11")

    assert(var12 == 2.0, s"process 1 has not variance 1.0 for dt = 1.0 but $var12")

  }

  "MarkovFunctionalStateProcess sp2 diffusion" should "eventually compute the diffusion" in {

    val dif21 = sp2.diffusion(0.0, 0.0)
    assert(Math.abs(dif21 - 1.0) <= tolerance, s"process 2 has wrong drift at 0.0, should be 1.0 but is $dif21")

    val dif22 = sp2.diffusion(0.99, 0.0)
    assert(Math.abs(dif22 - 1.0) <= tolerance, s"process 2 has wrong drift at 0.99, should be 1.0 but is $dif22")

    val dif23 = sp2.diffusion(1.0, 0.0)
    assert(Math.abs(dif23 - 2.0) <= tolerance, s"process 2 has wrong drift at 1.0, should be 2.0 but is $dif23")

    val dif24 = sp2.diffusion(1.9, 0.0)
    assert(Math.abs(dif24 - 2.0) <= tolerance, s"process 2 has wrong drift at 1.9, should be 2.0 but is $dif24")

    val dif25 = sp2.diffusion(2.0, 0.0)
    assert(Math.abs(dif25 - 3.0) <= tolerance, s"process 2 has wrong drift at 2.0, should be 3.0 but is $dif25")

    val dif26 = sp2.diffusion(3.0, 0.0)
    assert(Math.abs(dif26 - 3.0) <= tolerance, s"process 2 has wrong drift at 3.0, should be 3.0 but is $dif26")

    val dif27 = sp2.diffusion(5.0, 0.0)
    assert(Math.abs(dif27 - 3.0) <= tolerance, s"process 2 has wrong drift at 5.0, should be 3.0 but is $dif27")


  }
  "MarkovFunctionalStateProcess sp2 variance" should "eventually compute the variance" in {

    val var21 = sp2.variance(0.0, 0.0, 0.0)
    assert(Math.abs(var21 - 0.0) <= tolerance, s"process 2 has wrong variance at 0.0, should be 0.0 but is $var21")

    val var22 = sp2.variance(0.0, 0.0, 0.5)
    assert(Math.abs(var22 - 0.5) <= tolerance, s"process 2 has wrong variance at 0.5, should be 0.5 but is $var22")

    val var23 = sp2.variance(0.0, 0.0, 1.0)
    assert(Math.abs(var23 - 1.0) <= tolerance, s"process 2 has wrong variance at 1.0, should be 1.0 but is $var23")

    val var24 = sp2.variance(0.0, 0.0, 1.5)
    assert(Math.abs(var24 - 3.0) <= tolerance, s"process 2 has wrong variance at 1.5, should be 3.0 but is $var24")

    val var25 = sp2.variance(0.0, 0.0, 3.0)
    assert(Math.abs(var25 - 14.0) <= tolerance, s"process 2 has wrong variance at 3.0, should be 14.0 but is $var25")

    val var26 = sp2.variance(0.0, 0.0, 5.0)
    assert(Math.abs(var26 - 32.0) <= tolerance, s"process 2 has wrong variance at 5.0, should be 32.0 but is $var26")

    val var27 = sp2.variance(1.2, 0.0, 1.0)
    assert(Math.abs(var27 - 5.0) <= tolerance, s"process 2 has wrong variance between 1.2 and 2.2, should be 5.0 but is$var27")

  }


  "MarkovFunctionalStateProcess sp3 variance" should "eventually compute the variance" in {

    val var31 = sp3.variance(0.0, 0.0, 0.0)
    assert(Math.abs(var31 - 0.0) <= tolerance, s"process 3 has wrong variance at 0.0, should be 0.0 but is $var31")

    val var32 = sp3.variance(0.0, 0.0, 0.5)
    assert(Math.abs(var32 - 0.502508354208) <= tolerance, s"process 3 has wrong variance at 0.5, should be 0.5 but it is $var32")

    val var33 = sp3.variance(0.0, 0.0, 1.0)
    assert(Math.abs(var33 - 1.01006700134) <= tolerance, s"process 3 has wrong variance at 1.0, should be 1.0 but it is  $var33")

    val var34 = sp3.variance(0.0, 0.0, 1.5)
    assert(Math.abs(var34 - 3.06070578669) <= tolerance, s"process 3 has wrong variance at 1.5, should be 3.0 but it is $var34")

    val var35 = sp3.variance(0.0, 0.0, 3.0)
    assert(Math.abs(var35 - 14.5935513933) <= tolerance, s"process 3 has wrong variance at 3.0, should be 14.0 but it is $var35")

    val var36 = sp3.variance(0.0, 0.0, 5.0)

    assert(Math.abs(var36 - 34.0940185819) <= tolerance, s"process 3 has wrong variance at 5.0, should be 32.0 but it is $var36")

    val var37 = sp3.variance(1.2, 0.0, 1.0)
    assert(Math.abs(var37 - 5.18130257358) <= tolerance, s"process 3 has wrong variance between 1.2 and 2.2, should be 5.0 but it  $var37")


  }

}
