//preprocesseurs
#include <stdio.h>
#include <stdlib.h>
#include <time.h>


// succes variable
int success = 0;

//nombre de vie
int NOMBRE_DE_VIE = 5;

// function define at the end
void playGame(int);

int main()
{
    // user name variable
    char name [30] = "";

    //generate a random number
    srand(time(NULL));
    int number = rand()%100;      

    //introduction
    printf("\n\n*******SECRET_NUMBER_GAME*******\n\n");
    printf("Bienvenu les gars , nous comptons sur vous pour donner votre meilleur!\n\n\n"); 
    printf("Quel est ton nom combi ?\n");

    //retrieve user name
    scanf("%s",name);

    printf("\n-------------------------------------------------------\n");
    // user can play now
    playGame(number);

    //result
    if(success == 0) {
        printf("Desole %s tu as échoué retente ta chance , le nombre à deviner etait de : %d \n",name,number);
    }else
    {
        printf("Bravo %s, ton score est de %d/100 \n",name,NOMBRE_DE_VIE*20);
    }

    //decoration
    printf("\n-------------------------------------------------------\n");
    printf("-------------------------------------------------------\n");
    

    return 0;
}

void playGame(int Game_number) {

    int user_input = -1;

    //tant que le nombre de vie est superieur à zéro et l'utilisateur n'a pas trouvé le nombre généré continuez
    while ((user_input != Game_number) &&  NOMBRE_DE_VIE > 0)
    {
        //save user input
        printf("devine le nombre\n");
        scanf("%d",&user_input);

        if (user_input == Game_number)
        {
            success = 1;
            printf("Tu as trouvé le chiffre secret\n");

        }else if(user_input >Game_number) {

            printf("le nombre à deviner est plus petit que %d\n",user_input);
            NOMBRE_DE_VIE --;

        }else
        {
            printf("le nombre à deviner est plus grand que %d\n",user_input);
            NOMBRE_DE_VIE--;
        }

        //decoration
        printf("\n-------------------------------------------------------\n");       
        
    }
    
}