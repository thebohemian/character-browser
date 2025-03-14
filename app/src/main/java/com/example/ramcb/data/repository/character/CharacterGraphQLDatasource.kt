package com.example.ramcb.data.repository.character

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Query
import com.example.ramcb.data.repository.graphql.GetCharacterDetailsQuery
import com.example.ramcb.data.repository.graphql.GetCharactersQuery
import javax.inject.Inject

internal class CharacterGraphQLDatasource @Inject constructor(
    private val client: ApolloClient
) {

    suspend fun getCharacterDetails(id: String): Result<GetCharacterDetailsQuery.Data> =
        toResult(GetCharacterDetailsQuery(id))

    suspend fun getCharacters(page: Int): Result<GetCharactersQuery.Data> =
        toResult(GetCharactersQuery(page))

    private suspend fun <D : Query.Data> toResult(query: Query<D>): Result<D> =
        try {
            val response = client.query(query).execute()
            Result.success(response.dataOrThrow())
        } catch (e: Exception) {
            Result.failure(e)
        }
}