package dto

import SearchSystem

data class Request(val searcher: SearchSystem, val text: String)