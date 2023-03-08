package com.vote.data.util

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption

infix fun <T : Comparable<T>, S : T, C : Column<S>> C.cascadeReferences(
    ref: Column<T>
): C = apply {
    this.foreignKey = ForeignKeyConstraint(
        target = ref,
        from = this,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE,
        name = null
    )
}