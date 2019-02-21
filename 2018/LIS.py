

def getLIS(setInput):
	lis = []
	lis.append(1)

	for i in range(1,len(setInput)):
		max_value = 0
		for j in range(i-1,0):#go in reverse order
			if(setInput[i] > setInput[j] and lis[j] + 1 > max_value): constraint satisfied
				max_value = lis[j] + 1
		lis[i] = max_value

	return lis[len(setInput)-1]



def main():
	test_A = [1,2,3,4,5]
	test_B = [2,1,5,3,5,3,6]

	print(getLIS(test_A))
	print(getLIS(test_B))
main()