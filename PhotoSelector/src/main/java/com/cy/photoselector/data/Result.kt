package com.cy.photoselector.data

/**
 * A generic class that holds a value or error.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : com.cy.photoselector.data.Result<T>()
    data class Error(val exception: Exception) : com.cy.photoselector.data.Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is com.cy.photoselector.data.Result.Success<*> -> "Success[data=$data]"
            is com.cy.photoselector.data.Result.Error -> "Error[exception=$exception]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val com.cy.photoselector.data.Result<*>.succeeded
    get() = this is com.cy.photoselector.data.Result.Success && data != null