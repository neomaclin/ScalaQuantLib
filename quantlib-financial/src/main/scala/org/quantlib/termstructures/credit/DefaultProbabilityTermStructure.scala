//package org.quantlib.termstructures.credit
//
//import org.quantlib.quotes.Quote
//import org.quantlib.termstructures.TermStructure
//import org.quantlib.time.implicits.DateOps
//
///**
//  * Created by neo on 19/03/2017.
//  */
//
//
//final case class DefaultProbabilityTermStructure[D: DateOps](termStructure: TermStructure[D],
//                                                             jumps: List[Quote])
//
//final case class DefaultDensityStructure[D: DateOps](termStructure: TermStructure[D],
//                                                     jumps: List[Quote])
//
//final case class HazardRateStructure[D: DateOps](termStructure: TermStructure[D],
//                                                 jumps: List[Quote],
//                                                 jumpDates: List[D])
//
//final case class FlatHazardRate[D: DateOps](termStructure: TermStructure[D],
//                                            hazardRate: Quote)
//
////final case class InterpolatedDefaultDensityCurve[D: DateOps, I: Interpolator](termStructure: DefaultDensityStructure[D],
// //                                                               interpolator: InterpolatedCurve[I])
//
////final case class InterpolatedCurve[I: Interpolator](times: Seq[Double], data: Seq[Double], i: I)
//
////final case class SurvivalProbabilityStructure
//
//trait ProbabilityTrait
//
//trait SurvivalProbability extends ProbabilityTrait{
//
//}
//
//trait HazardRate extends ProbabilityTrait{
//
//}
//
//trait DefaultDensity extends ProbabilityTrait{
//
//
//
//}