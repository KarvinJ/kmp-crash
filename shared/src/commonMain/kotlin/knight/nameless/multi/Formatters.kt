package knight.nameless.multi

object Formatters {

    fun buildFormattedPhoneNumber(phoneNumber: String): String {

        val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")
        val formattedNumber = StringBuilder()

        for (i in digitsOnly.indices) {

            when (i) {
                0 -> formattedNumber.append("(")
                3 -> formattedNumber.append(") ")
                6 -> formattedNumber.append("-")
            }

            formattedNumber.append(digitsOnly[i])

            if (formattedNumber.length >= 14)
                break
        }

        return formattedNumber.toString()
    }
}