package knight.nameless.multi.data.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import knight.nameless.multi.domain.PersonModel

@Entity
data class Person(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val lastName: String,
    val phoneNumber: String
) {
    fun toPersonModel(): PersonModel {
        return PersonModel(id, name, lastName, phoneNumber)
    }
}
