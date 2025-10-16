package com.korashiGroup.salamtakPatient.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.korashiGroup.salamtakPatient.model.healthy.ItemHealth
import com.korashiGroup.salamtakPatient.repository.RegisterRepositry
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class DoctorsPagingSource(
    private val repo: RegisterRepositry,
    private val cityId: Int,
    private val areaId: Int,
    private val healthType: Int
) : PagingSource<Int, ItemHealth>() {
    override fun getRefreshKey(state: PagingState<Int, ItemHealth>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemHealth> {
        val currentPage = params.key ?: 0
        return try {
            val response = repo.getHealthEntityPagedList(cityId, areaId, healthType, 1, currentPage)
            if (response?.isSuccessful ?: false) {
                val data = response?.body()?.data?.items as List<ItemHealth>
                LoadResult.Page(
                    data = data,
                    prevKey = if (currentPage == 0) null else currentPage - 1,
                    nextKey = if (data.isEmpty()) null else currentPage + 1
                )
            } else {
                LoadResult.Error(IOException("Error loading data, code: ${response?.code()}"))

            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }


}