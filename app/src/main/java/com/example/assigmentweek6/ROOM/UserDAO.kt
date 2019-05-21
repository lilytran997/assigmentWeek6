package com.example.assigmentweek6.ROOM

import android.arch.persistence.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll():User

    @Query("SELECT * FROM user WHERE id=:id")
    fun findById(id: Int): User

    @Query("SELECT * FROM user WHERE name=:name")
    fun findByName(name: String): User

    @Insert
    fun insertAll(vararg todo: User): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(obj: User): Long

    @Delete
    fun delete(todo: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM user")
    fun deleteAllTask()
}