package com.masai.app.book_review.service;

import com.masai.app.book_review.DTO.FriendDTO;
import com.masai.app.book_review.DTO.Pair;
import com.masai.app.book_review.DTO.PairArray;
import com.masai.app.book_review.DTO.UserDTO;
import com.masai.app.book_review.Exception.DataInConsistency;
import com.masai.app.book_review.Exception.UserNotFound;
import com.masai.app.book_review.entity.FriendCircle;
import com.masai.app.book_review.entity.TransactionHistory;
import com.masai.app.book_review.entity.User;
import com.masai.app.book_review.modelmapper.ModelMapperClass;
import com.masai.app.book_review.repository.FriendCircleRepository;
import com.masai.app.book_review.repository.UserRepository;
import com.sun.net.httpserver.Authenticator;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class FriendCircleService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendCircleRepository friendCircleRepository;
    @Autowired
    UserService userService;

    @Autowired
    ModelMapperClass modelMapper;

    @Autowired
    FriendCircleService friendCircleService;
    
    @Autowired
    TransactionHistoryService transactionHistoryService;

    public List<FriendDTO> getAllFriendCircles() {
        List<FriendCircle> friendCircleList = friendCircleRepository.findAll();
        List<FriendDTO> allfriendDTO = modelMapper.modelMapper().map(friendCircleList, new TypeToken<List<FriendDTO>>() {
        }.getType());

        return allfriendDTO;
    }


 /*   public FriendCircle getSingleFriendCircle(String userNameId)
    {
        List<FriendCircle> friendCircleList = friendCircleRepository.findAll();

        Optional<FriendCircle> friendCircle = friendCircleRepository.findById(userNameId);

        if(friendCircle.isEmpty())
            return new FriendCircle();

        return  friendCircle.get();
    }  */


    // abhinav below one function
 /*   public FriendDTO addFriendCircle(FriendDTO fromFrienddto) {
        FriendCircle fromFriend = new FriendCircle();
        modelMapper.modelMapper().map(fromFrienddto, fromFriend);
        FriendCircle friendCircle = friendCircleRepository.save(fromFriend);
        modelMapper.modelMapper().map(friendCircle, fromFrienddto);

        return fromFrienddto;
    }   */
    
    @Transactional
    public FriendCircle addFriendCircle(FriendCircle fromFriend)
    {
        FriendCircle friendCircle = friendCircleRepository.save(fromFriend);

    //    System.out.println("Taker is "+ friendCircle.getToFriend());
    //    System.out.println("Giver is "+ friendCircle.getFromFriendId());


        if(userRepository.findById(friendCircle.getToFriend()).isPresent())
            addSingleFriendCircleForUserByIds(fromFriend.getToFriend(), fromFriend.getFromFriendId());

        return friendCircle;
    }


/*    public String updateFriendCircle(FriendCircle friendCircle)
    {
        Optional<FriendCircle> friendCircle1 = friendCircleRepository.findById(friendCircle.getFromFriendId());

        if(friendCircle1.isEmpty())
            return "FriendList does not exist";

        friendCircle1.get().setToFriend(friendCircle.getToFriend());
        friendCircle1.get().setAmount(friendCircle.getAmount());
        friendCircle1.get().setGiver(friendCircle.getGiver());
        friendCircle1.get().setTaker(friendCircle.getTaker());

        friendCircleRepository.save(friendCircle1.get());
        return  "Info Updated.";
    }   */


 /*   public String deleteFriendCircle(String friendCircleId)
    {
        FriendCircle friendCircle = friendCircleRepository.findById(friendCircleId).get();

        try
        {
            friendCircleRepository.delete(friendCircle);
            return "Deleted Successfully";
        }
        catch (Exception ex)
        {
            return "Delete Failed";
        }
    }  */

/*    @Transactional
    public void addFriendCircleListForUser(String userNameId, List<FriendCircle> friendCircleList)
    {
        User user = userRepository.findById(userNameId).get();  //handle user not found exception

        for(FriendCircle friendCircle: friendCircleList)
        {
            friendCircleRepository.save(friendCircle);
            friendCircle.setUser(user);
            user.addFriendCircle(friendCircle);

            friendCircleRepository.save(friendCircle);
        }

    }

    @Transactional
    public void addSingleFriendCircleForUser(String userNameId, FriendCircle friendCircle)
    {
        User user = userRepository.findById(userNameId).get(); //handle user not found exception

        friendCircleRepository.save(friendCircle);
        friendCircle.setUser(user);
        user.addFriendCircle(friendCircle);

        friendCircleRepository.save(friendCircle);
    }     */

    @Transactional
    public String addSingleFriendCircleForUserByIds(String userNameId, String friendCircleId) {
        System.out.println("userNameid is " + userNameId);
        System.out.println("friendCircleId is " + friendCircleId);

        //handle user not found exception
        if (userRepository.findById(userNameId).isPresent()) {
            User user = userRepository.findById(userNameId).get();

            List<FriendCircle> friendCircleList = friendCircleRepository.findAll();

            for (FriendCircle friendCircle : friendCircleList) {
                if ((friendCircle.getFromFriendId().equals(friendCircleId)) && (friendCircle.getToFriend().equals(userNameId))) {
                    System.out.println("Found 1 entry");
                    System.out.println(friendCircle);
                    //    friendCircleRepository.save(friendCircle);
                    friendCircle.setUser(user);
                    user.addFriendCircle(friendCircle);

                    friendCircleRepository.save(friendCircle);
                    System.out.println("friend Circle user is -> ");
                    System.out.println(friendCircle.getUser());
                    System.out.println(user.getFriendList().get(0));
                    System.out.println("Done till this");
                    return "Added to User";
                }
            }
            //     FriendCircle friendCircle = friendCircleRepository.findById(friendCircleId).get();

        }
        throw new UserNotFound("-------User Not Found---");

    }

    public String modifyFriendCircleByUserId(String userNameId, String friendCircleId, Integer amount) {
        //handle user not found exception
        //User user = userRepository.findById(userNameId).get();
        if (userRepository.findById(userNameId).isPresent()) {
            List<FriendCircle> friendCircleList = friendCircleRepository.findAll();

            for (FriendCircle friendCircle : friendCircleList) {
                if ((friendCircle.getFromFriendId().equals(friendCircleId)) && (friendCircle.getToFriend().equals(userNameId))) {
                    Integer currentAmount = friendCircle.getAmount();

                    if (amount > currentAmount) {
                        String msg = "Please enter an amount less than or equal to Rs. " + currentAmount;
                        return msg;
                    }

                    if (friendCircle.getGiver()) {
                        
                        TransactionHistory record = new TransactionHistory(null, userNameId, friendCircleId, amount, new Date());
                        transactionHistoryService.addRecord(record);
                        
                        Integer remainingAmount = currentAmount - amount;
                        friendCircle.setAmount(remainingAmount);

                        if (remainingAmount == 0) {
                            String msg = "Congrats! You have completely paid your part of the bill";
                            friendCircleRepository.save(friendCircle);
                            return msg;
                        } else {
                            String msg = "Rs. " + amount + "Paid Successfully. You still have to pay Rs. " + remainingAmount;
                            friendCircleRepository.save(friendCircle);
                            return msg;
                        }
                    }
                }
            }

        }

        //    FriendCircle friendCircle = friendCircleRepository.findById(friendCircleId).get();

        throw new UserNotFound("-------User Not Found---");

    }

    public String getListOfPayees(String friendId) {
        String msg = "";
        int j = 0;
        boolean found = false;
        List<FriendCircle> friendCircleList = friendCircleRepository.findAll();

        for (FriendCircle friendCircle : friendCircleList) {
            if (friendCircle.getFromFriendId().equals(friendId)) {
                found = true;
                ++j;
                //replace below with StringBuilder class
                msg += j + " ) " + friendCircle.getFromFriendId() + " has to give Rs. " + friendCircle.getAmount() + " to " + friendCircle.getUser().getPublicName() + " ( userName = " + friendCircle.getUser().getUserNameId() + " ) \n";
            }
        }

        if (found)
            return msg;
        else
            return "You dont have to give any money to anybody";



    /*    List<FriendCircle> friendCircleList = friendCircleRepository.findAll();
        String msg = "";
        int i = 0;
        for(FriendCircle friendCircle: friendCircleList)
        {
            ++i;
            User user = friendCircle.getUser();
            msg  +=  i + ")  +  " + friendId + " has to give Rs "+ friendCircle.getAmount() + " to "+ user.getPublicName();
        }
        return msg;   */

    }

    @Transactional
    public void test() {
   //     System.out.println("In test : ");

    //    System.out.println("For Rahul =>");
        System.out.println(userRepository.findById("Rahul123").get().getFriendList());

   //     System.out.println("For Bagul =>");
        System.out.println(userRepository.findById("Bagul123").get().getFriendList());

    }


    public String compute(HashMap<String, Integer> ComputedMap) {
       String str="";
        for (String name : ComputedMap.keySet()) {
            Integer p1amount = ComputedMap.get(name);
            if (p1amount > 0) {
                for (String tmpName : ComputedMap.keySet()) {
                    Integer p2amount = ComputedMap.get(tmpName);
                    if (!tmpName.equals(name) && p2amount < 0) {
                        Integer result = p1amount + p2amount;
                        ComputedMap.put(name, 0);
                        ComputedMap.put(tmpName, result);

                        System.out.println(tmpName + "=>" + p1amount + "=>" + name);
                        str+=tmpName + " has to pay " + p1amount + " to " + name+"\n";
                        if(userRepository.findById(name).isPresent())
                        {
                            System.out.println("I am here!");
                            FriendCircle friendCircle = new FriendCircle(null, tmpName, name, true, false, p1amount, null);
                            friendCircleService.addFriendCircle(friendCircle);
                        }
                        break;
                    }

                }
            }

        }
        return str;
    }

    public String addcontribution(Integer bill, Integer num, Character choice, List<Pair> pairList) {
String str="";
        int total_contribution = 0;
        int share = bill / num;
        //check correctness
        for (int i = 0; i < num; i++) total_contribution += pairList.get(i).getContribution();

        if (total_contribution != bill)
            throw new DataInConsistency("check entered data");

        if (choice == 'E') {

            HashMap<String, Integer> ComputedMap = new HashMap<>();
            for (int i = 0; i < num; i++) {
                String username = pairList.get(i).getUsername();
                int contribution = pairList.get(i).getContribution();
                ComputedMap.put(username, (Integer) contribution - share);
            }
            str+=compute(ComputedMap);
            for (String name : ComputedMap.keySet()) {
                if (ComputedMap.get(name) != 0) {
                   str+= compute(ComputedMap);
                }


            }

        }
        if(choice=='P'){
            for (int i = 0; i < num; i++) total_contribution += pairList.get(i).getContribution();

            if (total_contribution != 100)
                throw new DataInConsistency("check entered data");

            HashMap<String, Integer> ComputedMap = new HashMap<>();
            for (int i = 0; i < num; i++) {
                String username = pairList.get(i).getUsername();
                int percent_contribution = pairList.get(i).getContribution();
                int contribution = (percent_contribution*(bill))/100;
                ComputedMap.put(username, (Integer) contribution - share);

            }
            compute(ComputedMap);
            for (String name : ComputedMap.keySet()) {
                if (ComputedMap.get(name) != 0) {
                    compute(ComputedMap);
                }
            }
        }


        return str;
    }


    
    
    @Transactional
    public List<FriendCircle> getListOfPayors(String userId)
    {
        User user = userRepository.findById(userId).get();
        List<FriendCircle> friendCircleList = user.getFriendList();
        System.out.println("List of Payors: ");
        System.out.println(friendCircleList);
        return friendCircleList;
    }

}
