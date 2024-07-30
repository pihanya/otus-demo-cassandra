package ru.otus

import com.datastax.oss.driver.api.core.CqlSession
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.CassandraContainer
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRepositoryTest {

    companion object {

        private lateinit var cassandraContainer: CassandraContainer<Nothing>

        private lateinit var session: CqlSession

        @[BeforeAll JvmStatic]
        fun setUp() {
            cassandraContainer = CassandraContainer<Nothing>(/* dockerImageName = */ "cassandra:4.1.5")
                .apply { start() }

            session = CqlSession.builder()
                .addContactPoint(cassandraContainer.contactPoint)
                .withLocalDatacenter("datacenter1")
                .build()
                .apply {
                    execute("CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};")
                    execute("CREATE TABLE IF NOT EXISTS demo.users (id UUID PRIMARY KEY, name text, email text);")
                }
        }

        @[AfterAll JvmStatic]
        fun shutdown() {
            cassandraContainer.stop()
        }
    }

    @Test
    fun `should save and retrieve user`() {
        val userRepository = UserRepository(session)

        val userToSave = User(
            id = UUID.randomUUID(),
            name = "John",
            email = "john@example.com"
        )
        userRepository.save(userToSave)

        val foundUser = userRepository.findById(userToSave.id)

        assertEquals(userToSave, foundUser)
    }
}
