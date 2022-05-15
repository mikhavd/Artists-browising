package com.example.artists.networking

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphbrainz.herokuapp.com/graphql")
    .build()