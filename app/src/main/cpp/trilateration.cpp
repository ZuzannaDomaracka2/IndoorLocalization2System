#include <iostream>
#include <vector>
#include <utility>
#include <cmath>
#include <cassert>
#include <limits>
#include <chrono>

using std::vector;
using std::pair;

typedef pair<double, double> CoordinatesXY;
// [BEGIN] Mock code for testing (not required later)
class Beacon {
public:
	double PositionX;
	double PositionY;
	int Id;
	static int NextID;
	Beacon(CoordinatesXY coords) {
		PositionX = coords.first;
		PositionY = coords.second;
		Id = NextID++;
		std::cout << "Beacon pos (x,y) (" << PositionX << "," << PositionY << ") with ID: "<<Id<<" has been created.\n";
	}
};
int Beacon::NextID = 0;

// Create fixed location of Beacons
void AddBeacons(vector<Beacon>& Beacons) {
	CoordinatesXY coords(1.0,1.0);
	Beacons.push_back(Beacon(coords));
	coords = std::make_pair(11.0,1.0);
	Beacons.push_back(Beacon(coords));
	coords = std::make_pair(5.0,7.0);
	Beacons.push_back(Beacon(coords));
}

// Add distances from Beacons. Assuming distance at index 'x' in vector Distances  is distance from Beacon at index 'x' in Beacons vector.
// IMPORTANT NOTE DISTANCES ARE DISTANCES MAPPED TO 2D plane!
void AddDistances(vector<double>& Distances) {
	Distances.push_back(3.0);
	Distances.push_back(5.0);
	Distances.push_back(2.0);
}
// [END] Mock code for testing (not required later)
class Trilateration {
public:
	// control constants
	const int MAP_X_SIZE = 70; // X map size in meters
	const int MAP_Y_SIZE = 70; // Y map size in meters
	const double MAP_X_STEP = 0.1; // X size of smallest step (10cm) - resolution
	const double MAP_Y_STEP = 0.1; // Y size of smallest step (10cm) - resolution
	const int STEP_RESOLUTION = 4; // Number of counts per recursive step 4x4 in this case

	// control variables (for recursive trilateration)
	double currentMapSizeX; 
	double currentMapSizeY;
	double xTopBound;
	double xBottomBound;
	double yTopBound;
	double yBottomBound;
	Trilateration() {
		currentMapSizeX = MAP_X_SIZE;
		currentMapSizeY = MAP_Y_SIZE;
		xTopBound = MAP_X_SIZE;
		xBottomBound = 0;
		yTopBound = MAP_Y_SIZE;
		yBottomBound = 0;
	}

	// First attempt with circle equation. Pros - fast. Cons - is using only 3 distances.
	CoordinatesXY Trilaterate(const vector<double>& Distances, const vector<Beacon>& Beacons) {
		CoordinatesXY result;
		assert(Distances.size() >= 3u);
		assert(Beacons.size() >= 3u);
		assert(Beacons.size() == Distances.size());

		double A = -2.0 * Beacons[0].PositionX + 2.0 * Beacons[1].PositionX;
		double B = -2.0 * Beacons[0].PositionY + 2.0 * Beacons[1].PositionY;
		double C = pow(Distances[0], 2) - pow(Distances[1], 2) - pow(Beacons[0].PositionX, 2) + pow(Beacons[1].PositionX, 2) - pow(Beacons[0].PositionY, 2) + pow(Beacons[1].PositionY, 2);
		double D = -2.0 * Beacons[1].PositionX + 2.0 * Beacons[2].PositionX;
		double E = -2.0 * Beacons[1].PositionY + 2.0 * Beacons[2].PositionY;
		double F = pow(Distances[1], 2) - pow(Distances[2], 2) - pow(Beacons[1].PositionX, 2) + pow(Beacons[2].PositionX, 2) - pow(Beacons[1].PositionY, 2) + pow(Beacons[2].PositionY, 2);

		result.first = (C * E - F * B) / (E * A - B * D);
		result.second = (C * D - F * A) / (B * D - A * E);
		return result;
	}

	double getDistanceFromCircle(double distance,const Beacon& beacon, CoordinatesXY currentCoords) {
		double euclideanDistance = sqrt(pow(beacon.PositionX - currentCoords.first, 2.0) + pow(beacon.PositionY - currentCoords.second, 2.0));
		return fabs(euclideanDistance - distance);
	}

	CoordinatesXY RecursiveTrilateration(const vector<double>& Distances, const vector<Beacon>& Beacons) {
		assert(Distances.size() >= 3u);
		assert(Beacons.size() >= 3u);
		assert(Beacons.size() == Distances.size());
		bool debug = false;
		CoordinatesXY result(0, 0);
		std::pair<int, int> resLogicCoords(0, 0);
		double currentBestValue = std::numeric_limits<double>::max();

		// Count values for STEP_RESOLUTION x STEP_RESOLUTION points
		for (int i = 0; i < STEP_RESOLUTION; i++) {
			for (int j = 0; j < STEP_RESOLUTION; j++) {
				double currentValue = 0.0;
				double squareWidth = (xTopBound - xBottomBound) / STEP_RESOLUTION;
				double squareHeight = (yTopBound - yBottomBound) / STEP_RESOLUTION;
				CoordinatesXY currentCoords = CoordinatesXY(xBottomBound+(j+0.5)* squareWidth, yBottomBound+(i+ 0.5)* squareHeight);
				for (unsigned int k = 0; k < Distances.size(); k++) {
					currentValue += 1 / Distances[k] * pow(getDistanceFromCircle(Distances[k], Beacons[k], currentCoords), 2.0);
				}
				if (debug) printf("%5.5f ", currentValue);
				if (currentValue < currentBestValue) {
					currentBestValue = currentValue;
					result = currentCoords;
					resLogicCoords = std::make_pair(j, i);
				}
			}
			if (debug) std::cout<<std::endl;
		}

		if (debug)
			std::cout << "END OF ITERATION BEST ONE IS " << resLogicCoords.first << " " << resLogicCoords.second << std::endl;
		
		// Narrow boundries after decision for next recursive call
		xTopBound = xBottomBound + currentMapSizeX / STEP_RESOLUTION * (resLogicCoords.first + 1);
		xBottomBound = xBottomBound + currentMapSizeX / STEP_RESOLUTION * resLogicCoords.first;
		yTopBound = yBottomBound + currentMapSizeY / STEP_RESOLUTION * (resLogicCoords.second + 1);
		yBottomBound = yBottomBound + currentMapSizeY / STEP_RESOLUTION * resLogicCoords.second;
		currentMapSizeX = xTopBound - xBottomBound;
		currentMapSizeY = yTopBound - yBottomBound;
		if (debug) std::cout << "xTopBound: " << xTopBound << " yTopBound: " << yTopBound << " xBottomBound: " <<
			xBottomBound << " yBottomBOund: " << yBottomBound << " currentMapSizeX: "<< currentMapSizeX<<" currentMapSizeY: "<< currentMapSizeY<<"\n";
		// if boundries are smaller than step return result, else keep trilaterating
		if (currentMapSizeX <= MAP_X_STEP and currentMapSizeY <= MAP_Y_STEP) {
			std::cout << "Result is (x,y) (" << result.first << "," << result.second << ").\n";
			result.first = round(result.first / MAP_X_STEP) * MAP_X_STEP;
			result.second = round(result.second / MAP_Y_STEP) * MAP_Y_STEP;
			return result;
		}
		else return RecursiveTrilateration(Distances,Beacons);
	}

	CoordinatesXY TrilaterateLSM(const vector<double>& Distances, const vector<Beacon>& Beacons) {
		assert(Distances.size() >= 3u);
		assert(Beacons.size() >= 3u);
		assert(Beacons.size() == Distances.size());
		bool debug = false;
		CoordinatesXY result(0,0);
		double currentBestValue = std::numeric_limits<double>::max();
		
		for (int i = 0; i < MAP_Y_SIZE/MAP_Y_STEP; i++) {
			for (int j = 0; j < MAP_X_SIZE / MAP_X_STEP; j++) {
				double currentValue = 0.0;
				CoordinatesXY currentCoords = CoordinatesXY(j * MAP_Y_STEP, i * MAP_X_STEP);
				for (unsigned int k = 0; k < Distances.size();k++) {
					currentValue += 1/Distances[k]*pow(getDistanceFromCircle(Distances[k],Beacons[k], currentCoords), 2.0);
				}				
				if (debug) printf("%5f ", currentValue);
				if (currentValue < currentBestValue) {
					currentBestValue = currentValue;
					result = currentCoords;
				}
			}
			if (debug) std::cout<<std::endl;
		}		
		if (debug) std::cout << std::endl;
		return result;
	}
};

int main(int argc, char* argv) {
	std::cout << "Begin testing:\n";
	std::cout << "Adding beacons:\n";
	vector<Beacon> Beacons;
	AddBeacons(Beacons);

	std::cout << "Adding lengths:\n";
	vector<double> Distances;
	AddDistances(Distances);

	std::cout << "Create Trillateration object:\n";
	Trilateration* trillaterationObj = new Trilateration();
	// We can use this result as reference
	CoordinatesXY result = trillaterationObj->TrilaterateLSM(Distances, Beacons);

	std::chrono::high_resolution_clock::time_point t1 = std::chrono::high_resolution_clock::now();
	std::cout << "Reference result is (x,y) (" << result.first << "," << result.second << ").\n";
	result = trillaterationObj->RecursiveTrilateration(Distances, Beacons);
	std::chrono::high_resolution_clock::time_point t2 = std::chrono::high_resolution_clock::now();
	std::chrono::duration<double> time_span = std::chrono::duration_cast<std::chrono::duration<double>>(t2 - t1);
	std::cout << "It took me " << time_span.count() << " seconds.";
	std::cout << std::endl;
	std::cout << "Final result is (x,y) (" << result.first << "," << result.second << ").\n";
	return 0;
}