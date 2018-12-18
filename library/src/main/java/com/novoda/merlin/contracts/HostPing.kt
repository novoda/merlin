package com.novoda.merlin.contracts

import java.net.URL

interface Endpoint {

    fun asURL(): URL

}

interface ResponseCodeValidator {

    fun isResponseCodeValid(responseCode: Int): Boolean

    class CaptivePortalResponseCodeValidator : ResponseCodeValidator {
        override fun isResponseCodeValid(responseCode: Int): Boolean {
            return responseCode == 204
        }
    }
}
