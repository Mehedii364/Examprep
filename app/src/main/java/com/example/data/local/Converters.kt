package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionSection
import com.example.data.model.RepeatType

class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    @TypeConverter
    fun fromLongList(value: List<Long>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toLongList(value: String?): List<Long> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split("|||")
    }

    @TypeConverter
    fun fromSection(value: QuestionSection?): String {
        return value?.name ?: QuestionSection.KA.name
    }

    @TypeConverter
    fun toSection(value: String?): QuestionSection {
        return try {
            QuestionSection.valueOf(value ?: QuestionSection.KA.name)
        } catch (e: Exception) {
            QuestionSection.KA
        }
    }

    @TypeConverter
    fun fromPriority(value: PriorityLevel?): String {
        return value?.name ?: PriorityLevel.HIGH.name
    }

    @TypeConverter
    fun toPriority(value: String?): PriorityLevel {
        return try {
            PriorityLevel.valueOf(value ?: PriorityLevel.HIGH.name)
        } catch (e: Exception) {
            PriorityLevel.HIGH
        }
    }

    @TypeConverter
    fun fromRepeatType(value: RepeatType?): String {
        return value?.name ?: RepeatType.UNIQUE.name
    }

    @TypeConverter
    fun toRepeatType(value: String?): RepeatType {
        return try {
            RepeatType.valueOf(value ?: RepeatType.UNIQUE.name)
        } catch (e: Exception) {
            RepeatType.UNIQUE
        }
    }
}
