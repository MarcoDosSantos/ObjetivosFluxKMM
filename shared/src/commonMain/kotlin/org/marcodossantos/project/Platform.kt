package org.marcodossantos.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform