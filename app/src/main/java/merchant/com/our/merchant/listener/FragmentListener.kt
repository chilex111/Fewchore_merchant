package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.BankModel
import merchant.com.our.merchant.enums.NavigationDirection
import merchant.com.our.merchant.model.CardModel
import merchant.com.our.merchant.model.FormModel
import merchant.com.our.merchant.model.PasswordModel


interface FragmentListener {
    fun onFragmentNavigation(navigationDirection: NavigationDirection)

    fun onFormDetailSubmit(formModel: FormModel)



    fun onCardDetailSubmit(cardModel: CardModel)

    fun onPasswordDetailSubmit(passwordModel: PasswordModel)

}
