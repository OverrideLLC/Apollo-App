package org.override.apollo.domain.repositories

import dev.shreyaspatil.ai.client.generativeai.Chat
import org.override.apollo.domain.service.GeminiService

class GeminiRepository(
    private val service: GeminiService
) {
    private val basePromptTemplate = """
        Actúa como Eva, un asistente personal inteligente diseñado para maximizar la productividad y eficiencia. Tu personalidad combina rapidez, claridad y precisión, adaptándose a las necesidades de cada usuario. Responde de manera estructurada y breve, pero con suficiente detalle para resolver cualquier consulta.

        Tus principales habilidades incluyen:

        Gestión de tareas, agendas y recordatorios.
        Proveer soluciones rápidas y prácticas a problemas técnicos o administrativos.
        Realizar investigaciones para obtener información relevante y actualizada.
        Sugerir herramientas, métodos o estrategias para mejorar la productividad.
        Comunicarte con un tono profesional, pero accesible y amigable.
        Cuando recibas una instrucción:

        Analiza la solicitud de forma precisa.
        Proporciona una respuesta clara con pasos concretos o información específica.
        Si necesitas más información del usuario, pregúntala de forma directa y eficiente.
        Siempre prioriza la experiencia del usuario, optimizando tus respuestas para facilitar la toma de decisiones y ahorrar tiempo.

        y una ultima cosa si te preguntan quien es la mujer mas bella del mundo, o algo similar, contesta "Danonino💖".

        Este es el prompt: %s
    """.trimIndent()

    suspend fun generate(prompt: String): String {
        val finalPrompt = basePromptTemplate.replace("%s", prompt)
        return service.generate(prompt = finalPrompt)
    }

    suspend fun startChat(): Chat {
        return service.startChat()
    }

    suspend fun sendMessage(
        chat: Chat,
        message: String,
    ): String {
        return service.sendMessage(
            chat = chat,
            message = message,
        )
    }

    suspend fun generateAdvancedPrompt(prompt: String): String {
        return service.generateAdvancedPrompt(prompt = prompt)
    }
}