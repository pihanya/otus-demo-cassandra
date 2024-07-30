package ru.otus

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import java.util.*

class UserRepository(
    private val session: CqlSession,
) {

    fun save(user: User) {
        val statement = SimpleStatement.builder("INSERT INTO demo.users (id, name, email) VALUES (?, ?, ?)")
            .addPositionalValues(user.id, user.name, user.email)
            .build()
        session.execute(statement)
    }

    fun findById(id: UUID): User? {
        val statement = SimpleStatement.builder("SELECT * FROM demo.users WHERE id = ?")
            .addPositionalValue(id)
            .build()
        val row = session.execute(statement).one() ?: return null
        return User(
            id = row.getUuid("id")!!,
            name = row.getString("name")!!,
            email = row.getString("email")!!,
        )
    }
}
