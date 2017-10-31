package org.iskopasi.salchart.utils

import java.util.regex.Pattern

/**
 * Created by cora32 on 31.10.2017.
 */
object RegexUtils {
    private var SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]")

    fun escapeSpecialRegexChars(str: String): String {
        return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0")
    }
}