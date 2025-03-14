package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.graphql.GetCharacterDetailsQuery
import com.example.ramcb.data.repository.model.CharacterStatus
import com.example.ramcb.data.repository.model.Gender
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class CharacterDetailsMapperTest {

    @Before
    fun before() {
        mockkObject(CharacterStatusMapper, GenderMapper)
    }

    @Test
    fun `should map simple attributes`() {
        // given:
        val givenId = "id"
        val givenName = "name"
        val givenType = "type"
        val givenSpecies = "species"
        val givenImageUrl = "imageUrl"

        val given = mockk<GetCharacterDetailsQuery.Data>()
        every { given.character } returns mockk {
            every { id } returns givenId
            every { name } returns givenName
            every { species } returns givenSpecies
            every { type } returns givenType
            every { imageUrl } returns givenImageUrl

            every { status } returns ""
            every { gender } returns ""
        }

        // when:
        val result = CharacterDetailsMapper(given)

        // then:
        shouldNotBeNull {
            Result
        }

        with(result) {
            id shouldBe givenId
            name shouldBe givenName
            species shouldBe givenSpecies
            type shouldBe givenType
            imageUrl shouldBe givenImageUrl
        }
    }

    @Test
    fun `should map status using CharacterStatusMapper`() {
        // given:
        val givenStatus = "status"
        val givenCharacterStatus = mockk<CharacterStatus>()
        every { CharacterStatusMapper(any()) } returns givenCharacterStatus

        val given = mockk<GetCharacterDetailsQuery.Data>()
        every { given.character } returns mockk {
            every { status } returns givenStatus

            every { id } returns ""
            every { name } returns ""
            every { species } returns ""
            every { type } returns ""
            every { imageUrl } returns ""
            every { gender } returns ""
        }

        // when:
        val result = CharacterDetailsMapper(given)

        // then:
        verify { CharacterStatusMapper(givenStatus) }

        with(result) {
            status shouldBe givenCharacterStatus
        }
    }

    @Test
    fun `should map gender using GenderMapper`() {
        // given:
        val givenGender = "gender"
        val givenGenderModel = mockk<Gender>()
        every { GenderMapper(any()) } returns givenGenderModel

        val given = mockk<GetCharacterDetailsQuery.Data>()
        every { given.character } returns mockk {
            every { gender } returns givenGender

            every { id } returns ""
            every { name } returns ""
            every { species } returns ""
            every { type } returns ""
            every { imageUrl } returns ""
            every { status } returns ""
        }

        // when:
        val result = CharacterDetailsMapper(given)

        // then:
        verify { GenderMapper(givenGender) }

        with(result) {
            gender shouldBe givenGenderModel
        }
    }

    @Test
    fun `should throw when character not present`() {
        // given:
        val given = mockk<GetCharacterDetailsQuery.Data>()
        every { given.character } returns null

        // when:
        val result = {
            CharacterDetailsMapper(given)
        }

        // then:
        shouldThrowAny(result)
    }

    @Test
    fun `should throw when character id not present`() {
        // TODO: Make parametrized and check for all properties

        // given:
        val given = mockk<GetCharacterDetailsQuery.Data>()
        every { given.character!!.id } returns null

        // when:
        val result = {
            CharacterDetailsMapper(given)
        }

        // then:
        shouldThrowAny(result)
    }

}