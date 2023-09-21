package kotlinsql.annotations

@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey(val name: String = "")
