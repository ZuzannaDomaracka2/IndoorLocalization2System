#include <cstdlib>
#include <iostream>
#include <vector>
//extern "C" JNIEXPORT jstring JNICALL
using namespace std;

vector<double> sr_ruchoma(vector<double> vector1, int ilosc_probek)
{
    vector<double> wynik;
    double suma = 0;
    int licznik = 0;
    for (int i = 0; i < (int)vector1.size(); i++) {
        suma += vector1[i]; 
        licznik++;
        if (licznik >= ilosc_probek) {
            wynik.push_back(suma / (double)ilosc_probek);
            suma -= vector1[licznik - ilosc_probek];
        }
    }
    return wynik;
}


int main()
{

    vector<double> vector1 {53.24, 54.38, 65.33, 43.24, 34.38, 70.3};
    vector<double> wynik;
    int ilosc_probek = 2;

    wynik = sr_ruchoma(vector1, ilosc_probek);
    for (int i = 0; i < (int)wynik.size(); i++)
        cout << wynik.at(i) << endl;

    return 0;
}

