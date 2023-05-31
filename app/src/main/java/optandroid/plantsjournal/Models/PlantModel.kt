package optandroid.plantsjournal.Models

data class PlantModel (
        var description : String ?= null,
        var id : String ?= null,
        var imageURL: String ?= null,
        var name : String ?= null,
        var plantSize: String ?= null,
        var purchaseDate : String ?= null,
        var purchasePlace: String ?= null
        )