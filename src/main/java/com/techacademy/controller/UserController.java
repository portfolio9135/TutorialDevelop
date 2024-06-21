package com.techacademy.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.entity.User;
import com.techacademy.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    
    
    
    
    
    
    
    
    

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        model.addAttribute("userlist", service.getUserList());
        return "user/list";
    }
    
    
    
    
    
    
    
    
    

    /** User登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute User user) {
        return "user/register";
    }

    /** User登録処理 */
    @PostMapping("/register")
    public String postRegister(@Validated User user, BindingResult res, Model model) {
        if (res.hasErrors()) {
            return getRegister(user);
        }
        service.saveUser(user);
        return "redirect:/user/list";
    }









    /** User更新画面を表示 */
    @GetMapping("/update/{id}/")
    public String getUser(@PathVariable(value = "id", required = false) Integer id, Model model) {

        //一覧画面から「更新」ボタン押した時は
    	//URLに含まれているidを使ってデータベースからユーザー情報を取得して、更新画面に表示するので

    	//if文でidがある場合は「service.getUser(id)」で、そのidに対応するユーザー情報をデータベースから取得する。
    	//その取得したユーザー情報を引数にしてaddAttributeメソッドを実行。　モデルに登録することでビューで使えるようにしておく。
        if (id != null) {
            model.addAttribute("user", service.getUser(id));

            //postUser()から遷移したとき（IDがない場合）、つまりフォームにエラーがあって、入力データを保持したまま更新画面に戻す場合
            //model.getAttribute("user")で、モデルから"user"というキーで保持しているユーザー情報を取り出す。

            //それ引数にしてaddAttributeメソッドを実行。つまり取り出したユーザー情報をもう一回modelに登録するということ。
            //モデルに登録することでビューで使えるようにしておく。
            //これで、エラーメッセージと共にユーザーが入力したデータが保持されて更新画面に戻る
        } else {
            model.addAttribute("user", model.getAttribute("user"));
        }

        //処理の最後にどっちの場合でも共通で、user/updateというビュー（テンプレート）を表示する
        return "user/update";
    }



    /** User更新処理 */
    //POSTリクエストが飛んできたときに呼び出されるメソッド
    //フォームから送信されたユーザー情報を受け取って更新処理を行う
    @PostMapping("/update/{id}/")

    //【postUserメソッドの引数の説明まとめ】
    //@Validated User user: フォームから送信されたユーザー情報をバリデーションする。
    //BindingResult res: バリデーションの結果を格納するオブジェクト。
    //@PathVariable("id") Integer id: URLパスからユーザーIDを取得する。
    //Model model: ビューにデータを渡すためのオブジェクト。
    public String postUser(@Validated User user, BindingResult res, @PathVariable("id") Integer id, Model model) {

    	//「res.hasErrors()がtrue」＝ バリデーションエラーがあったということなので
    	//model.addAttribute("user", user)で、 フォームから送信されたユーザー情報（エラー含む）をモデルに登録する。
    	//戻り値として、
        if (res.hasErrors()) {
            model.addAttribute("user", user);
            return getUser(null, model);
        }

        //入力されたuserオブジェクトに、URLから取得したIDを設定する。これで、どのユーザーを更新するかが明確になる
        user.setId(id);

        //service.saveUser(user);を呼び出して、ユーザーの情報をデータベースに保存する。これで、入力された値がデータベースに反映される
        service.saveUser(user);

        //最後に、return "redirect:/user/list";で、ユーザーの一覧画面にリダイレクトする
        return "redirect:/user/list";
    }









    /** User削除処理 */
    @PostMapping(path="list", params="deleteRun")
    public String deleteRun(@RequestParam(name="idck") Set<Integer> idck, Model model) {
        service.deleteUser(idck);
        return "redirect:/user/list";
    }
}
