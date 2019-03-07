package merchant.com.our.merchant.listener

import android.text.Editable
import merchant.com.our.merchant.enums.CardValidity


interface PayStackCardValidationListener {
    fun afterChange(cardValidity: CardValidity, editable: Editable)

    fun paramIsValid(z: Boolean, cardValidity: CardValidity)
}
