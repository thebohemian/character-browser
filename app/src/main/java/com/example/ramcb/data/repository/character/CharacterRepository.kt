package com.example.ramcb.data.repository.character

import com.example.ramcb.IoDispatcher
import com.example.ramcb.data.repository.character.mapper.CharacterDetailsMapper
import com.example.ramcb.data.repository.character.mapper.PaginatedCharacterPreviewListMapper
import com.example.ramcb.data.repository.model.CharacterDetails
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.data.repository.model.Paginated
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class CharacterRepository @Inject constructor(
    private val datasource: CharacterGraphQLDatasource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun getCharacterDetails(id: String): Flow<LoadResult<CharacterDetails>> = flow {
        emit(LoadResult.Loading)

        try {
            val model =
                CharacterDetailsMapper(datasource.getCharacterDetails(id).getOrThrow())
            emit(LoadResult.Success(model))
        } catch (e: Exception) {
            emit(LoadResult.Error(e))
        }
    }.flowOn(dispatcher)

    fun getCharacterPreviews(page: Int): Flow<LoadResult<Paginated<List<CharacterPreview>>>> =
        flow {
            emit(LoadResult.Loading)

            try {
                val model = PaginatedCharacterPreviewListMapper(
                    datasource.getCharacters(page).getOrThrow(),
                    page
                )
                emit(LoadResult.Success(model))
            } catch (e: Exception) {
                emit(LoadResult.Error(e))
            }
        }.flowOn(dispatcher)


}