#include <cstdlib>
#include <iostream>
#include <vector>
#include <jni.h>
//extern "C" JNIEXPORT jstring JNICALL
using namespace std;

double variance(vector<double> vector1)
{
    double variance = 0.0;
    double average = 0.0;
    double sum = 0.0;
    int size = (int)vector1.size();
    for (int i = 0; i < size; i++)
    {
        sum += vector1[i];
    }

    average = sum / size;

    for (int i = 0; i < size; i++)
    {
        variance += (vector1[i] - average) * (vector1[i] - average);
    }
    return variance / size;
}

vector <double> Fusion(vector<double>varianceT, vector<double> varianceF, vector<double>varianceD, bool deadReckoning, vector<double> TabT, vector <double> TabF, vector<double> TabD, vector<double> Tab)
{
    int sizeT = varianceT.size();
    int sizeF = varianceF.size();
    int sizeD = varianceD.size();
    double FinalVarianceT = 0.0;
    double FinalVarianceF = 0.0;
    double FinalVarianceD = 0.0;
    double X = 0.0;
    double Y = 0.0;

    if (deadReckoning) {
        for (int i = 0; i < sizeT; i++) {
            FinalVarianceT += varianceT[i];
        }
        FinalVarianceT = FinalVarianceT / sizeT;

        for (int i = 0; i < sizeF; i++) {
            FinalVarianceF += varianceF[i];
        }
        FinalVarianceF = FinalVarianceF / sizeF;

        for (int i = 0; i < sizeD; i++) {
            FinalVarianceD += varianceD[i];
        }

        FinalVarianceD = FinalVarianceD / sizeD;

        double v1 = (FinalVarianceF + FinalVarianceT) / (2*(FinalVarianceD + FinalVarianceF + FinalVarianceT));
        double v2 = (FinalVarianceD + FinalVarianceT) / (2*(FinalVarianceD + FinalVarianceF + FinalVarianceT));
        double v3 = (FinalVarianceF + FinalVarianceD) / (2*(FinalVarianceD + FinalVarianceF + FinalVarianceT));
        X = v1 * TabT[0] + v2 * TabF[0] + v3 * TabD[0];
        Y = v1 * TabT[1] + v2 * TabF[1] + v3 * TabD[1];
        Tab.push_back(X);
        Tab.push_back(Y);

        return Tab;
    }
    else {
        for (int i = 0; i < sizeT; i++) {
            FinalVarianceT = +varianceT[i];
        }
        FinalVarianceT = FinalVarianceT / sizeT;

        for (int i = 0; i < sizeF; i++) {
            FinalVarianceF = +varianceF[i];
        }
        FinalVarianceF = FinalVarianceF / sizeF;

        double v1 = (FinalVarianceT) / (FinalVarianceF + FinalVarianceT);
        double v2 = (FinalVarianceD) / (FinalVarianceF + FinalVarianceT);

        X = v1 * TabT[0] + v2 * TabF[0];
        Y = v1 * TabT[1] + v2 * TabF[1];
        Tab.push_back(X);
        Tab.push_back(Y);
        return Tab;
    }
}

int main()
{
    vector<double> vector1{ 53.24, 54.38, 65.33, 43.24, 34.38, 70.3 };

    vector<double> TabT{3,4}; // tablice koordynatów z różych metod
    vector<double> TabF{2,5};
    vector<double> TabD{6,7};
    vector<double> Tab;// tablica do wyników-ostateczne koordynaty

    vector<double> varianceTrilateration{1,7,4}; //wektor wariancji z pomiarów mocy czujników, które brały udział przy obliczaniu koordynatów
    vector<double> varianceFingerprinting{6,3,9}; //wektor wariancji z pomiarów mocy czujników, które brały udział przy obliczaniu koordynatów
    vector<double> varianceDeadReckoning{2,2,3}; //wektor wariancji z pomiarów mocy czujników, które brały udział przy obliczaniu koordynatów
    bool DeadReckoningAvilable = true;
 
    cout << endl << variance(vector1);
    Tab = Fusion(varianceTrilateration, varianceFingerprinting, varianceDeadReckoning, DeadReckoningAvilable, TabT, TabF, TabD, Tab);
    for (int i = 0; i < 2; i++)
        cout << endl << Tab.at(i) << endl;
    return 0;
}
