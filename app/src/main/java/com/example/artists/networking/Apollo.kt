package com.example.artists.networking

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    //todo check if "/graphql" part is NEEDED
    .serverUrl("https://graphbrainz.herokuapp.com/graphql") //todo "https://apollo-fullstack-tutorial.herokuapp.com/graphql")
    .build()