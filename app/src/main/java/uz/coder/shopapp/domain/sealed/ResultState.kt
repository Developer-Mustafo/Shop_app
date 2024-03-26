package uz.coder.shopapp.domain.sealed

sealed class ResultState {
    data class ErrorName(val enabled:Boolean, val message:String):ResultState()
    data class ErrorCount(val enabled:Boolean, val message:String):ResultState()
    data class Finish(val u: Unit) : ResultState()
}