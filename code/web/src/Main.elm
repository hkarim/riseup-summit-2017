import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import WebSocket
import Material.Table as Table
import Json.Decode exposing (..)

main : Program String Model Msg
main =
    Html.programWithFlags
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        } 

-- MODEL

type alias City =
    { id : Int
    , name : String
    , countryCode : String
    , district : String
    , population : Int
    }

type alias Model = 
    {
        cities: List City
    ,   wsUrl: String
    }


initialModel : String -> Model
initialModel url =
    {
        cities = []
    ,   wsUrl = url
    }


init : String -> ( Model, Cmd Msg )
init url =
    ( initialModel url, Cmd.none )



-- UPDATE


type Msg
    = Send | WsMessage (Result String City)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Send ->
            (model, WebSocket.send model.wsUrl "Hello, server!")
        WsMessage a ->
            case a of
                Result.Ok city ->
                    ({ model | cities = city :: model.cities }, Cmd.none)
                Result.Err err ->
                    (model, Cmd.none )        
            



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
    WebSocket.listen model.wsUrl (decodeString decodeCity)
    |> Sub.map WsMessage

-- VIEW


view : Model -> Html Msg
view model =
   let 
        table = 
            Html.div [style [("padding-top", "10px")]]
                [
                    Table.table []
                    [ Table.thead []
                    [ Table.tr []
                        [ Table.th [] [ text "ID" ]
                        , Table.th [] [ text "NAME" ]
                        , Table.th [] [ text "COUNTRY CODE" ]
                        , Table.th [] [ text "DISTRICT" ]
                        , Table.th [] [ text "POPULATION" ]
                        ]
                    ]
                    , Table.tbody [] (citiesView model.cities)
                    ]
                ]
            
    in
        Html.div [style [("padding-top", "10px"), ("margin-left", "20%")]]
            [
                Html.button [class "mdl-js-button mdl-button mdl-button--raised", onClick Send] [text "START"]
                , table
            ]
    

citiesView: List City -> List (Html msg)
citiesView cities =
    List.map cityView cities

cityView: City -> Html msg
cityView city = 
    Table.tr []
        [ Table.td [ Table.numeric ] [ text <| toString <| city.id ]
        , Table.td [] [ text city.name ]
        , Table.td [] [ text city.countryCode ]
        , Table.td [] [ text city.district ]
        , Table.td [ Table.numeric ] [ text <| toString <| city.population ]
        ]

decodeCity : Json.Decode.Decoder City
decodeCity =
    Json.Decode.map5 City
        (field "id" Json.Decode.int)
        (field "name" Json.Decode.string)
        (field "countryCode" Json.Decode.string)
        (field "district" Json.Decode.string)
        (field "population" Json.Decode.int)
