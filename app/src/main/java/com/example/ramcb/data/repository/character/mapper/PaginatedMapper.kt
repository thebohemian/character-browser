package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.graphql.GetCharactersQuery
import com.example.ramcb.data.repository.model.Paginated

object PaginatedMapper {
    operator fun <D> invoke(pageInfo: GetCharactersQuery.PageInfo, page: Int, data: D) = Paginated(
        page = page, pageCount = pageInfo.count!! / 20, data = data
    )

}