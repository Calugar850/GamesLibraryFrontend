package com.example.frontendv5


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This activity implement a page for main features. Display the games, add game in cart, access the forms,
 * sort games by some parameters.
 * The BASE_URL is a constant value which is used for communication with backend.
 */
const val BASE_URL = "http://10.0.2.2:8080/"
class FirstActivity : AppCompatActivity() {
    var game: Button? = null
    var bill: Button? = null
    var userDetails: Button? = null
    lateinit var games: ArrayList<Game>
    var gamesIntermediar = arrayListOf<Game>()
    var years = arrayListOf<String>()
    var gens = arrayListOf<String>()
    var publishers = arrayListOf<String>()
    var spinner: Spinner? = null
    var spinner2: Spinner? = null
    var spinner3: Spinner? = null
    var publisher: Boolean = false;
    var gen: Boolean = false;
    var year: Boolean = false;
    var publisherS: String = "";
    var genS: String = "";
    var yearS: String = "";

    /**
     * This is a method which is called when this activity is created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        getGameProperties();

        games = Game.createGameList()
        displayGames();
        setupUI();
        setupListeners();
    }

    /**
     * Method to find all the publishers and display in the dropdown only ine time
     */
    private fun findPublishers(){
        publishers.add("Publishers")
        for(g in games){
            publishers.add(g.publisher!!)
        }
        publishers = publishers.toSet().toList() as ArrayList<String>
        val spinnerArrayAdapter: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, publishers)
        spinner = findViewById(R.id.publisher)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner?.setAdapter(spinnerArrayAdapter)
    }

    /**
     * Method to find all the gens of the games and display in the dropdown only ine time
     */
    private fun findGens() {
        gens.add("Gen")
        for(g in games){
            gens.add(g.gen!!)
        }
        gens = gens.toSet().toList() as ArrayList<String>
        val spinnerArrayAdapter1: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, gens)
        spinner2 = findViewById(R.id.gen)
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2?.setAdapter(spinnerArrayAdapter1)
    }

    /**
     * Method to find all the release years and display in the dropdown only ine time
     */
    private fun findYears() {
        years.add("Year")
        for(g in games){
            years.add(g.anAparitie.toString())
        }
        years = years.toSet().toList() as ArrayList<String>
        val spinnerArrayAdapter2: ArrayAdapter<*> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        spinner3 = findViewById(R.id.year)
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3?.setAdapter(spinnerArrayAdapter2)
    }

    /**
     * Method to display all games into a recycler view
     */
    private fun displayGames() {
        if(year){
            // Lookup the recyclerview in activity layout
            val rvGames = findViewById<View>(R.id.games) as RecyclerView
            for(g in games){
                if(g.anAparitie.toString().equals(yearS)){
                    gamesIntermediar.add(g)
                }
            }
            // Create adapter passing in the sample user data
            val adapter = GameAdapter(gamesIntermediar)
            // Attach the adapter to the recyclerview to populate items
            rvGames.adapter = adapter
            // Set layout manager to position the items
            rvGames.layoutManager = LinearLayoutManager(this)
        }else if(gen){
            // Lookup the recyclerview in activity layout
            val rvGames = findViewById<View>(R.id.games) as RecyclerView
            for(g in games){
                if(g.gen.equals(genS)){
                    gamesIntermediar.add(g)
                }
            }
            // Create adapter passing in the sample user data
            val adapter = GameAdapter(gamesIntermediar)
            // Attach the adapter to the recyclerview to populate items
            rvGames.adapter = adapter
            // Set layout manager to position the items
            rvGames.layoutManager = LinearLayoutManager(this)
        }else if(publisher){
            // Lookup the recyclerview in activity layout
            val rvGames = findViewById<View>(R.id.games) as RecyclerView
            for(g in games){
                if(g.publisher.equals(publisherS)){
                    gamesIntermediar.add(g)
                }
            }
            // Create adapter passing in the sample user data
            val adapter = GameAdapter(gamesIntermediar)
            // Attach the adapter to the recyclerview to populate items
            rvGames.adapter = adapter
            // Set layout manager to position the items
            rvGames.layoutManager = LinearLayoutManager(this)
        } else{
            // Lookup the recyclerview in activity layout
            val rvGames = findViewById<View>(R.id.games) as RecyclerView
            // Create adapter passing in the sample user data
            val adapter = GameAdapter(games)
            // Attach the adapter to the recyclerview to populate items
            rvGames.adapter = adapter
            // Set layout manager to position the items
            rvGames.layoutManager = LinearLayoutManager(this)
        }

    }

    /**
     * Method for find the buttons of this activity
     */
    private fun setupUI() {
        game = findViewById(R.id.create_game) as Button
        bill = findViewById(R.id.rcreate_bill) as Button
        userDetails = findViewById(R.id.user_detail) as Button
    }

    /**
     * Method to instantiate the listeners for buttons
     */
    private fun setupListeners() {
        game!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                createGameForm()
            }
        })

        bill!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                createBillForm()
            }
        })

        userDetails!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                val context: Context = view!!.getContext()
                val i = Intent(context, UserDetailsActivity::class.java)
                context.startActivity(i)
            }
        })
    }

    /**
     * Redirect user to bill form to create a bill
     */
    private fun createBillForm() {
        val i = Intent(this, CreateBillActivity::class.java)
        startActivity(i)
    }

    /**
     * Redirect user to game form to create add a game
     */
    private fun createGameForm() {
        val i = Intent(this, CreateGameActivity::class.java)
        startActivity(i)
    }

    /**
     * Method make a request to get all games from database
     */
    private fun getGameProperties() {

        val specs = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

        val client = OkHttpClient.Builder()
            .connectionSpecs(specs)
            .build()

        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl(BASE_URL).build().create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<Game2>?> {
            override fun onResponse(
                call: Call<List<Game2>?>,
                response: Response<List<Game2>?>
            ) {
                val responseBody = response.body()!!
                for(Data in responseBody)
                {
                    println(Data.toString()+"\n")
                    val g = Game(Data.idGame, Data.nume, Data.anAparitie, Data.publisher, Data.pret, Data.gen, Data.descriere)
                    games.add(g)
                }
                findPublishers()
                findGens()
                findYears()
                displayGames()

                sortByPublisher()
                sortByGen()
                sortByYear()
            }

            override fun onFailure(call: Call<List<Game2>?>, t: Throwable) {
                Log.d(ContentValues.TAG,"onFailure: " +t.message)
            }
        })
    }

    /**
     * Method to sort all games by a years of release
     */
    private fun sortByYear() {
        spinner3?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedItemText = parentView!!.getItemAtPosition(position) as String
                // Display the selected item into the TextView
                // Display the selected item into the TextView
                println("Selected : $selectedItemText")
                if(position!=0){
                    gamesIntermediar.removeAll(gamesIntermediar)
                    yearS=selectedItemText
                    year=true
                    displayGames()
                }else{
                    year=false
                    displayGames()
                }

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    /**
     * Method to sort all games by gen
     */
    private fun sortByGen() {
        spinner2?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedItemText = parentView!!.getItemAtPosition(position) as String
                // Display the selected item into the TextView
                // Display the selected item into the TextView
                println("Selected : $selectedItemText")
                if(position!=0){
                    gamesIntermediar.removeAll(gamesIntermediar)
                    genS=selectedItemText
                    gen=true;
                    displayGames()
                }else{
                    gen=false;
                    displayGames()
                }

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    /**
     * Method to sort all games by a publisher
     */
    private fun sortByPublisher() {
        spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedItemText = parentView!!.getItemAtPosition(position) as String
                // Display the selected item into the TextView
                // Display the selected item into the TextView
                println("Selected : $selectedItemText")
                if(position!=0){
                    gamesIntermediar.removeAll(gamesIntermediar)
                    publisherS=selectedItemText;
                    publisher=true;
                    displayGames()
                }else{
                    publisher=false;
                    displayGames()
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

}