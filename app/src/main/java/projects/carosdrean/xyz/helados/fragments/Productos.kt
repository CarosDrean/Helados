package projects.carosdrean.xyz.helados.fragments


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import projects.carosdrean.xyz.helados.R


/**
 * A simple [Fragment] subclass.
 */
class Productos : Fragment() {

    var appBarLayout: AppBarLayout? = null
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_paginado, container, false)
        if(savedInstanceState == null){
            insertartabs(container!!)
            viewPager = v.findViewById(R.id.pager)
            poblarViewPager(viewPager!!)
            tabLayout?.setupWithViewPager(viewPager)
        }
        return v
    }

    override fun onDestroy() {
        super.onDestroy()
        appBarLayout?.removeView(tabLayout)
    }

    fun insertartabs(container: ViewGroup){
        val padre = container.parent as View
        appBarLayout = padre.findViewById(R.id.appbar)
        tabLayout = TabLayout(activity)
        tabLayout?.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"))
        appBarLayout?.addView(tabLayout)
    }

    fun poblarViewPager(viewPager: ViewPager){
        val adapter = AdaptadorSecciones(fragmentManager)
        adapter.addFragment(Categorias.nuevaInstancia(0), getString(R.string.helados_pequeños))
        adapter.addFragment(Categorias.nuevaInstancia(1), getString(R.string.helados_grandes))
        viewPager.adapter = adapter
    }

    class AdaptadorSecciones(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
        val fragmentos: MutableList<Fragment> = ArrayList()
        val titulos: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentos.get(position)
        }

        override fun getCount(): Int {
            return fragmentos.size
        }

        fun addFragment(fragment: Fragment, title: String){
            fragmentos.add(fragment)
            titulos.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titulos.get(position)
        }

    }

}// Required empty public constructor
