package org.quantlib.process

/**
  * Created by neo on 26/02/2017.
  */

trait GeneralizedBlackScholesProcess extends  StochasticProcess1D

trait BlackScholesProcess extends GeneralizedBlackScholesProcess

trait GarmanKohlagenProcess extends GeneralizedBlackScholesProcess

trait BlackScholesMertonProcess extends  GeneralizedBlackScholesProcess

trait BlackProcess extends GeneralizedBlackScholesProcess