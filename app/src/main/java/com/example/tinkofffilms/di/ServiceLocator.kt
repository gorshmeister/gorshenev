package com.example.tinkofffilms.di

import com.example.tinkofffilms.data.database.AppDataBase
import com.example.tinkofffilms.data.network.FilmsApi
import com.example.tinkofffilms.data.network.NetworkService
import com.example.tinkofffilms.data.repository.FilmsRepository
import com.example.tinkofffilms.data.repository.IFilmsRepository
import kotlinx.coroutines.Dispatchers

object ServiceLocator {

    private val db: AppDataBase = AppDataBase.getDatabase()

    private val api: FilmsApi = NetworkService.api

    private val dispatcher = Dispatchers.IO

    val repository: IFilmsRepository = FilmsRepository(db, api, dispatcher)

}