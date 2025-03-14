package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.graphql.GetCharactersQuery
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.Paginated

object PaginatedCharacterPreviewListMapper {
    operator fun invoke(
        data: GetCharactersQuery.Data,
        page: Int
    ): Paginated<List<CharacterPreview>> =
        with(data.characters!!) {
            PaginatedMapper(pageInfo!!, page, toCharacterPreviewList(results ?: emptyList()))
        }

    private fun toCharacterPreviewList(results: List<GetCharactersQuery.Result?>): List<CharacterPreview> =
        results.filterNotNull().map(CharacterPreviewMapper::invoke)

}