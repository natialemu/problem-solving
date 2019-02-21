
def isPalindrome(word):
	pointer1 = 0
	pointer2 = len(word) - 1
	if len(word)%2 == 0 :
		return False
	else:
		for pointer1 in range(len(word)//2):
			#print('pointer 1 is at ', word[pointer1], 'and pointer2 is at ', word[pointer2])
			if(word[pointer1] != word[pointer2]):
				return False
			pointer2 = pointer2 - 1
	return True

def main():

    print('Test for isPlaindrome: ')
    #print('psp is a palindrome should be true:-> outcome is ', isPalindrome('psp'))
    palindrome1 = "dont nod in a top spot"
    palindrome = palindrome1.replace(' ','')
    print(palindrome)
    K = [0]*(len(palindrome)+1)
    A = [""]*(len(palindrome)+1)
    K[-1] = 0
    K[-2] = 1
    A[-2] = palindrome[-1]
    print(A[-2])
    lengthOfPalindrome = len(palindrome)
    lengthOfPalindromeToIterate = len(palindrome) - 1
    for i in reversed(range(0,lengthOfPalindrome-1)):
        #print("considering index: ", i)
        minPalindrome  = 100
        minStringPalindrome = ""
        minStringIndex = -1
        for j in range(i, lengthOfPalindrome):#
            
            if(isPalindrome(palindrome[i:j+1]) and minPalindrome > K[j+1]+1):
                #print("the word ",palindrome[i:j+1], " is a palindrome")
                minStringPalindrome = palindrome[i:j+1]
                minStringIndex = j+1
                minPalindrome = K[j+1] + 1

        
        K[i] = minPalindrome
        A[i] = "{0} {1}".format(minStringPalindrome,A[minStringIndex])
        print(minStringPalindrome, "----->",A[i])
    print("the minimum number of palindromes is ", K[0])
    print("the string with minimum palindromes is ", A[0])

main()