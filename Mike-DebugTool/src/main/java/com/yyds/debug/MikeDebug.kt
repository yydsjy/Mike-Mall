package com.yyds.debug
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MikeDebug(val name:String,val desc:String)