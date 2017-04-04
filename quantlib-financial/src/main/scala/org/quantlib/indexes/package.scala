package org.quantlib

/**
  * Created by neo on 02/04/2017.
  */
package object indexes {
  implicit class Index[I: IndexT](val indexT: I){
    val impl = implicitly[IndexT[I]]

    def name:String = impl.name(indexT)
  }
}
