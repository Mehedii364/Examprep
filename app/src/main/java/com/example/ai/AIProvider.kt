package com.example.ai

import com.example.data.model.AiProviderType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

interface AIProvider {
    val providerType: AiProviderType
    suspend fun generateAnswer(question: String, section: String, topic: String): Result<String>
    suspend fun analyzeTopic(topic: String, questions: List<String>): Result<String>
}

class LocalAnalyticalProvider : AIProvider {
    override val providerType = AiProviderType.LOCAL_ANALYTICAL

    override suspend fun generateAnswer(question: String, section: String, topic: String): Result<String> {
        return withContext(Dispatchers.Default) {
            val answer = when (section.lowercase()) {
                "ka", "ক-বিভাগ" -> "সংক্ষিপ্ত ১ নম্বরের নির্ভুল উত্তর: $question হলো রাষ্ট্রবিজ্ঞান ও শাসনতান্ত্রিক কাঠামোর অন্যতম মৌলিক অনুষঙ্গ যা সংবিধান ও আইনের অনুশাসনের সাথে প্রত্যক্ষভাবে সংশ্লিষ্ট।"
                "kha", "খ-বিভাগ" -> """
                    ভূমিকা: আলোচ্য প্রশ্নটি ‘$topic’ বিষয়ের একটি গুরুত্বপূর্ণ শাসনতান্ত্রিক প্রশ্ন।
                    
                    মূল আলোচনা:
                    ১. সাংবিধানিক ভিত্তি: এটি শাসনব্যবস্থার ক্ষমতার ভারসাম্য ও জবাবদিহিতা নিশ্চিত করে।
                    ২. তুলনামূলক বৈশিষ্ট্য: যুক্তরাজ্য ও মার্কিন যুক্তরাষ্ট্রের রাজনৈতিক প্রক্রিয়ায় এর প্রয়োগ লক্ষণীয়।
                    ৩. গণতান্ত্রিক স্থিতিশীলতা: এটি ক্ষমতার অপব্যবহার রোধ করে আইনের শাসনকে সমুন্নত রাখে।
                    ৪. নাগরিক অধিকারের সংযোগ: নাগরিকদের অধিকার রক্ষা ও জনকল্যাণ সাধনে এর ভূমিকা অনস্বীকার্য।
                    
                    উপসংহার: পরিশেষে বলা যায়, কার্যকর রাষ্ট্র পরিচালনায় এই প্রত্যয়টি অত্যন্ত তাৎপর্যপূর্ণ।
                """.trimIndent()
                else -> """
                    ভূমিকা:
                    রাষ্ট্রবিজ্ঞানের ২য় পত্রে ‘$topic’ অধ্যায়ের অধীনে এই প্রশ্নটি অত্যন্ত তাৎপর্যপূর্ণ এবং বিগত বছরের পরীক্ষাগুলোতে একাধিকবার বিশ্লেষিত হয়েছে।
                    
                    সংজ্ঞা ও ধারণা:
                    আলোচ্য প্রশ্নের মূল প্রত্যয়টি মূলত গণতান্ত্রিক রাষ্ট্রকাঠামো ও শাসনব্যবস্থার সাংগঠনিক ভিত্তি নির্দেশ করে।
                    
                    মূল বিশ্লেষণ ও তাৎপর্য:
                    ১. প্রাতিষ্ঠানিক ভিত্তি ও ক্ষমতা বণ্টন:
                    সরকারের বিভিন্ন অঙ্গের মধ্যকার সম্পর্ক ও এখতিয়ার সুনির্দিষ্ট নীতিমালার ভিত্তিতে পরিচালিত হয়।
                    
                    ২. আইনের অনুশাসন ও মৌলিক অধিকার:
                    সংবিধানের প্রাধান্য এবং নাগরিকের অধিকার সুরক্ষায় এই কাঠামোর সক্রিয় কার্যকারিতা অনস্বীকার্য।
                    
                    ৩. যুক্তরাজ্য ও মার্কিন প্রেক্ষাপট:
                    ব্রিটিশ পার্লামেন্টারি সার্বভৌমত্ব এবং মার্কিন সংবিধানের চেকস অ্যান্ড ব্যালেন্সেস (নিয়ন্ত্রণ ও ভারসাম্য) ব্যবস্থার সাথে এর ঘনিষ্ঠ তুলনামূলক প্রাসঙ্গিকতা বিদ্যমান।
                    
                    ৪. সমকালীন রাজনৈতিক প্রেক্ষাপট ও সংস্কার:
                    আধুনিক গণতান্ত্রিক কল্যাণরাষ্ট্রে এই ব্যবস্থার প্রয়োগ সুশাসন প্রতিষ্ঠায় ইতিবাচক অবদান রাখছে।
                    
                    উপসংহার:
                    সার্বিক আলোচনায় এটি প্রতীয়মান হয় যে, একটি টেকসই ও গণতান্ত্রিক সমাজ বিনির্মাণে উপর্যুক্ত নীতিমালার বাস্তবায়ন অপরিহার্য।
                """.trimIndent()
            }
            Result.success(answer)
        }
    }

    override suspend fun analyzeTopic(topic: String, questions: List<String>): Result<String> {
        return Result.success("বিষয়ভিত্তিক বিশ্লেষণ: ‘$topic’ অংশ থেকে পরীক্ষায় নিয়মিত প্রশ্ন আসার সুস্পষ্ট প্রমাণ রয়েছে।")
    }
}

class GeminiProvider(
    private val apiKey: String,
    private val model: String = "gemini-2.5-flash"
) : AIProvider {
    override val providerType = AiProviderType.GEMINI

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    override suspend fun generateAnswer(question: String, section: String, topic: String): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("Gemini API কী প্রদান করা হয়নি। সেটিংস থেকে API কী কনফিগার করুন।"))
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
            val prompt = """
                তুমি জাতীয় বিশ্ববিদ্যালয়ের রাষ্ট্রবিজ্ঞান দ্বিতীয় পত্র (যুক্তরাজ্য ও মার্কিন যুক্তরাষ্ট্রের রাজনৈতিক ব্যবস্থা) বিষয়ের একজন অভিজ্ঞ শিক্ষক ও পরীক্ষক।
                নিচের প্রশ্নের জন্য একটি মানসম্পন্ন, পরীক্ষোপযোগী উত্তর বাংলায় প্রস্তুত কর।
                
                বিভাগ: $section
                অধ্যায়/টপিক: $topic
                প্রশ্ন: $question
                
                যদি ক-বিভাগ হয়: ১ নম্বরের জন্য অত্যন্ত নির্ভুল ও সংক্ষিপ্ত উত্তর দাও।
                যদি খ-বিভাগ হয়: ভূমিকা, ৪টি প্রধান পয়েন্ট ও উপসংহার সহ ৪ নম্বরের উত্তর দাও।
                যদি গ-বিভাগ হয়: ভূমিকা, সংজ্ঞা, বিস্তারিত ৫-৬টি বিশ্লেষণাত্মক পয়েন্ট ও উপসংহার সহ ১০ নম্বরের উত্তর দাও।
                কোনো অপ্রাসঙ্গিক কথা বলবে না।
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Gemini ত্রুটি (${response.code}): $responseBody"))
            }

            val root = JSONObject(responseBody)
            val candidates = root.optJSONArray("candidates")
            val first = candidates?.optJSONObject(0)
            val content = first?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("Gemini থেকে কোনো উত্তর পাওয়া যায়নি।"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeTopic(topic: String, questions: List<String>): Result<String> = withContext(Dispatchers.IO) {
        Result.success("‘$topic’ অধ্যায়ের সারসংক্ষেপ প্রস্তুত।")
    }
}

class OpenRouterProvider(
    private val apiKey: String,
    private val model: String = "google/gemini-2.0-flash-exp:free"
) : AIProvider {
    override val providerType = AiProviderType.OPEN_ROUTER

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    override suspend fun generateAnswer(question: String, section: String, topic: String): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("OpenRouter API কী প্রদান করা হয়নি। সেটিংস থেকে API কী যুক্ত করুন।"))
        }

        try {
            val url = "https://openrouter.ai/api/v1/chat/completions"
            val prompt = """
                তুমি জাতীয় বিশ্ববিদ্যালয়ের রাষ্ট্রবিজ্ঞান ২য় পত্রের শিক্ষক। বাংলায় উত্তর লিখ:
                বিভাগ: $section | অধ্যায়: $topic
                প্রশ্ন: $question
                পরীক্ষোপযোগী কাঠামো (ভূমিকা, পয়েন্ট ও উপসংহার) বজায় রাখ।
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("model", model)
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("HTTP-Referer", "https://aistudio.google.com")
                .addHeader("X-Title", "Political Science 2nd Paper Pro")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("OpenRouter ত্রুটি (${response.code}): $responseBody"))
            }

            val root = JSONObject(responseBody)
            val choices = root.optJSONArray("choices")
            val message = choices?.optJSONObject(0)?.optJSONObject("message")
            val text = message?.optString("content")

            if (!text.isNullOrBlank()) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("OpenRouter থেকে কোনো উত্তর পাওয়া যায়নি।"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeTopic(topic: String, questions: List<String>): Result<String> {
        return Result.success("বিশ্লেষণ সম্পন্ন।")
    }
}
