package knight.nameless.multi.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class Person(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val phoneNumber: String
)
